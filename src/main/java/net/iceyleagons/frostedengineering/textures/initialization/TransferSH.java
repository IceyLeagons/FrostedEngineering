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

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class TransferSH implements IUploadable {

    @Override
    public String[] keywords() {
        return new String[]{"transfer", "sh", "transfer.sh", "t.sh", "shell"};
    }

    @Override
    public void init() {
        common(file -> {
            try (Response response = new OkHttpClient().newCall(new Request.Builder()
                    .url("https://transfer.sh/" + file.getName())
                    .put(RequestBody.create(MediaType.parse("application/zip"), file))
                    .build()).execute()) {
                String url = response.message();

                Main.info(Optional.of("Textures"), "Resource pack uploaded.");
                Main.info(Optional.of("Textures"),
                        "Resource pack link is: " + url);
                Main.info(Optional.of("Textures"), "Calculating SHA-1 hash...");
                byte[] hash = sha1Code(file);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Main.info(Optional.of("Textures"),
                                "Resource pack hash is: " + bytesToHexString(hash));

                        Textures.setData("resourcepack-link", url);
                        Textures.hash = hash;

                        Bukkit.getOnlinePlayers().forEach(player -> player
                                .setResourcePack(Textures.getData("resourcepack-link"), Textures.hash));
                    }
                }, 1000L);
            } catch (IOException ignored) {

            }
        });
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
