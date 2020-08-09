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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.textures.Textures;
import net.iceyleagons.frostedengineering.textures.interfaces.IUploadable;
import org.bukkit.Bukkit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.time.Duration;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class tmpNinja implements IUploadable {

    @Override
    public String[] keywords() {
        return new String[]{"tmpninja", "tmp", "ninja", "tninja", "tmpn", "temporary", "primary"};
    }

    @Override
    public void init() {
        common(file -> {
            // Create a new htmlunit driver in selenium.
            WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME, true) {
                @Override
                public WebClient modifyWebClient(final WebClient modify) {
                    return modify(modify);
                }
            };

            // Get the tmp.ninja page
            driver.get("https://tmp.ninja/");

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    driver.findElement(By.name("file")).sendKeys(file.getAbsolutePath());
                    driver.findElement(By.xpath("/html/body/div[1]/form/button")).click();

                    WebElement element = fluentWait(By.xpath("/html/body/div[1]/div/div/div/div/p/a"), driver);

                    Main.info(Optional.of("Textures"), "Resource pack uploaded.");
                    Main.info(Optional.of("Textures"),
                            "Resource pack link is: " + element.getText());
                    Main.info(Optional.of("Textures"), "Calculating SHA-1 hash...");
                    String hash = sha1Code(file);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Main.info(Optional.of("Textures"),
                                    "Resource pack hash is: " + hash);

                            Textures.setData("resourcepack-link", element.getText());
                            Textures.setData("resourcepack-hash", hash);

                            Bukkit.getOnlinePlayers().forEach(player -> player
                                    .setResourcePack(Textures.getData("resourcepack-link"), Textures.getData("resourcepack-hash")));
                        }
                    }, 1000L);
                }
            }, 10000L);
        });
    }

    @Override
    public boolean needReupload() {
        return true;
    }

    @Override
    public Duration reuploadIntervals() {
        return Duration.ofDays(2);
    }
}
