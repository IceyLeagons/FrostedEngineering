/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.textures.initialization;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.textures.Textures;
import net.iceyleagons.frostedengineering.textures.interfaces.IUploadable;
import okhttp3.*;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ZeroxZero implements IUploadable {

    @Override
    public String[] keywords() {
        return new String[]{"0x0", "zero", "zxz", "primary"};
    }

    @Override
    public void init() {
        common(file -> {
            String stringHash = Textures.getData("resourcepack-hash");
            byte[] currentHash = sha1Code(file);
            String stringCurrentHash = bytesToHexString(currentHash);
            if (stringHash != null) {
                if (!stringHash.equalsIgnoreCase(stringCurrentHash))
                    a(file, currentHash, stringCurrentHash);
            } else
                a(file, currentHash, stringCurrentHash);
        });
    }

    private void a(File file, byte[] hash, String stringHash) {
        try (Response response = new OkHttpClient().newCall(new Request.Builder().url("https://0x0.st/")
                .post(new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(MediaType.parse("application/zip"), file))
                        .build()).build()).execute()) {
            final String url = Objects.requireNonNull(response.body()).string().replace("|", "")
                    .replace("\r", "").replace("\n", "").replace(" ", "");
            Main.info(Optional.of("Textures"), "Resource pack uploaded.");
            Main.info(Optional.of("Textures"),
                    "Resource pack link is: " + url);
            Main.info(Optional.of("Textures"),
                    "Resource pack hash is: " + stringHash);

            Textures.setData("resourcepack-link", url);
            Textures.setData("resourcepack-hash", stringHash);
            Textures.hash = hash;

            Bukkit.getOnlinePlayers().forEach(player -> player
                    .setResourcePack(Textures.getData("resourcepack-link"), Textures.hash));
        } catch (IOException ignored) {

        }
    }
}
