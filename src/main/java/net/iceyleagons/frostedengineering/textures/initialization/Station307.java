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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Station307 implements IUploadable {

    @Override
    public String[] keywords() {
        return new String[]{"experimental", "station", "station307", "307", "s307"};
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init() {
        common(file -> {
            new Timer().schedule(new TimerTask() {
                @SneakyThrows
                @Override
                public void run() {
                    String url = new OkHttpClient().newCall(new Request.Builder()
                            .url("https://www.station307.com/")
                            .put(RequestBody.create(MediaType.parse("application/zip"), file))
                            .build()).execute().header("com.station307.located-at");

                    Main.executor.execute(new Runnable() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            while (Main.MAIN.isEnabled()) {
                                assert url != null;
                                new OkHttpClient().newCall(new Request.Builder()
                                        .url(url)
                                        .put(RequestBody.create(MediaType.parse("application/zip"), file))
                                        .build()).execute();
                            }
                        }
                    });

                    Main.info(Optional.of("Textures"), "Resource pack uploaded.");
                    Main.info(Optional.of("Textures"),
                            "Resource pack link is: " + url);
                    Main.info(Optional.of("Textures"), "Calculating SHA-1 hash...");
                    String hash = sha1Code(file);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Main.info(Optional.of("Textures"),
                                    "Resource pack hash is: " + hash);

                            Textures.setData("resourcepack-link", url);
                            Textures.setData("resourcepack-hash", hash);

                            Bukkit.getOnlinePlayers().forEach(player -> player
                                    .setResourcePack(Textures.getData("resourcepack-link"), Textures.getData("resourcepack-hash")));
                        }
                    }, 1000L);
                }
            }, 10000L);
        });
    }

}
