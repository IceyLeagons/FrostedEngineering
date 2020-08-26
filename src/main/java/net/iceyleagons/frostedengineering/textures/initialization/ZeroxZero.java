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

import lombok.SneakyThrows;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.textures.Textures;
import net.iceyleagons.frostedengineering.textures.interfaces.IUploadable;
import okhttp3.*;
import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZeroxZero implements IUploadable {

    @Override
    public String[] keywords() {
        return new String[]{"0x0", "zero", "zxz", "primary"};
    }

    @Override
    public void init() {
        common(file -> new Timer().schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                try (Response response = new OkHttpClient().newCall(new Request.Builder().url("https://0x0.st/")
                        .post(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", file.getName(),
                                        RequestBody.create(MediaType.parse("application/zip"), file))
                                .build()).build()).execute()) {
                    String url = response.body().string().replace("|", "").replace("\r", "").replace("\n", "").replace(" ", "");

                    Main.info(Optional.of("Textures"), "Resource pack uploaded.");
                    Main.info(Optional.of("Textures"),
                            "Resource pack link is: " + url);
                    Main.info(Optional.of("Textures"), "Calculating SHA-1 hash...");
                    byte[] hash = sha1Code(file);
                    String finalUrl = url;
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Main.info(Optional.of("Textures"),
                                    "Resource pack hash is: " + bytesToHexString(hash));

                            Textures.setData("resourcepack-link", finalUrl);
                            Textures.hash = hash;

                            Bukkit.getOnlinePlayers().forEach(player -> player
                                    .setResourcePack(Textures.getData("resourcepack-link"), Textures.hash));
                        }
                    }, 1000L);
                }
            }
        }, 10000L));
    }

    @Override
    public boolean needReupload() {
        return true;
    }

    @Override
    public Duration reuploadIntervals() {
        return Duration.ofDays(14);
    }
}
