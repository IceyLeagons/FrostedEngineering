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
import java.util.Objects;
import java.util.Optional;

public class Station307 implements IUploadable {

    @Override
    public String[] keywords() {
        return new String[]{"experimental", "station", "station307", "307", "s307"};
    }

    @Override
    public void init() {
        common((file, hash, stringHash) -> {
            try (Response response = new OkHttpClient().newCall(new Request.Builder()
                    .url("https://www.station307.com/")
                    .put(RequestBody.create(MediaType.parse("application/zip"), file))
                    .build()).execute()) {

                String url = response.header("com.station307.located-at");

                Main.executor.execute(() -> {
                    final String finalUrl = Objects.requireNonNull(url);

                    while (Main.MAIN.isEnabled())
                        try (Response ignored = new OkHttpClient().newCall(new Request.Builder()
                                .url(finalUrl)
                                .put(RequestBody.create(MediaType.parse("application/zip"), file))
                                .build()).execute()) {
                            // Not even needed.
                        } catch (IOException ignored) {

                        }
                });

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
        });
    }

}
