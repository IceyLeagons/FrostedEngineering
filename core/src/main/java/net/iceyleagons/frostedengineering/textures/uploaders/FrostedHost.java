package net.iceyleagons.frostedengineering.textures.uploaders;

import lombok.SneakyThrows;
import net.iceyleagons.frostedengineering.textures.ITextureUploader;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class FrostedHost implements ITextureUploader {

    public String fileHostUrl = "http://localhost:8080/";

    public long lastUpdated;

    @Override
    public Map.Entry<String, byte[]> upload(File file) {
        try {
            JSONObject jsonObject = new JSONObject(new OkHttpClient.Builder()
                    .build()
                    .newCall(new Request.Builder()
                            .url(fileHostUrl)
                            .post(new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file", file.getName(), RequestBody.create(file, MediaType.parse("application/zip")))
                                    .build())
                            .build())
                    .execute()
                    .body().string());

            lastUpdated = System.currentTimeMillis();

            return new AbstractMap.SimpleImmutableEntry<>(fileHostUrl + "files/" + jsonObject.getString("id"), sha1Code(file));
        } catch (NullPointerException | IOException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public long lastUpload() {
        return lastUpdated;
    }
}
