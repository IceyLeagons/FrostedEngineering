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
package net.iceyleagons.frostedengineering.textures.interfaces;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.IncorrectnessListener;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserListener;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.textures.Textures;
import net.iceyleagons.frostedengineering.textures.base.TexturedBase;
import net.iceyleagons.frostedengineering.textures.events.TextureSetupEvent;
import net.iceyleagons.frostedengineering.textures.initialization.McMeta;
import net.iceyleagons.frostedengineering.textures.initialization.Sounds;
import net.iceyleagons.frostedengineering.textures.initialization.Sounds.SoundData;

public interface IUploadable {

    public void init();

    public default void upload(File file) {
        // Create a new htmlunit driver in selenium.
        WebDriver driver = new HtmlUnitDriver(BrowserVersion.CHROME, true) {
            @Override
            public WebClient modifyWebClient(final WebClient modify) {
                return modify(modify);
            }
        };

        // Get the minepack.net page
        driver.get("https://minepack.net/");

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                driver.findElement(By.name("resourcepack")).sendKeys(file.getAbsolutePath());
                driver.findElement(By.name("submit")).submit();

                List<WebElement> webElements = driver.findElements(By.name("select"));

                Main.info(Optional.of("Textures"), "Resource pack uploaded.");
                Main.info(Optional.of("Textures"),
                        "Resource pack link is: " + webElements.get(0).getAttribute("value"));
                Main.info(Optional.of("Textures"),
                        "Resource pack hash is: " + webElements.get(1).getAttribute("value"));

                Textures.setData("resourcepack-link", webElements.get(0).getAttribute("value"));
                Textures.setData("resourcepack-sha1", webElements.get(1).getAttribute("value"));

                Bukkit.getOnlinePlayers().forEach(player -> player
                        .setResourcePack(Textures.getData("resourcepack-link"), Textures.getData("resourcepack-sha1")));

                driver.close();
            }
        }, 10000L);
    }

    public default WebClient modify(final WebClient client) {
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getCookieManager().setCookiesEnabled(true);
        client.setRefreshHandler(new ThreadedRefreshHandler());
        client.getOptions().setDownloadImages(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);
        client.getOptions().setRedirectEnabled(true);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);

        client.setIncorrectnessListener(new IncorrectnessListener() {

            @Override
            public void notify(String arg0, Object arg1) {
                // Ignored.
            }
        });
        client.setJavaScriptErrorListener(new JavaScriptErrorListener() {

            @Override
            public void timeoutError(HtmlPage arg0, long arg1, long arg2) {
                // Ignored.
            }

            @Override
            public void scriptException(HtmlPage arg0, ScriptException arg1) {
                // Ignored.
            }

            @Override
            public void malformedScriptURL(HtmlPage arg0, String arg1, MalformedURLException arg2) {
                // Ignored.
            }

            @Override
            public void loadScriptError(HtmlPage arg0, URL arg1, Exception arg2) {
                // Ignored.
            }

            @Override
            public void warn(String arg0, String arg1, int arg2, String arg3, int arg4) {
                // Ignored.
            }
        });
        client.setHTMLParserListener(new HTMLParserListener() {

            @Override
            public void error(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {
                // Ignored.
            }

            @Override
            public void warning(String arg0, URL arg1, String arg2, int arg3, int arg4, String arg5) {
                // Ignored.
            }
        });

        return client;
    }

    public default void unzipFile(File assets, File where) {
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(assets));
            ZipEntry entry = zipIn.getNextEntry();
            while (entry != null) {
                Main.debug("Extracting entry " + entry.getName() + " from the zip file " + assets.getName());
                if (!entry.isDirectory())
                    extractEntry(zipIn, new File(where, entry.getName()));
                else
                    createFolder(new File(where, entry.getName()));
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public default void extractEntry(ZipInputStream zipIn, File file) throws IOException {
        FileOutputStream fOS = new FileOutputStream(file);
        byte[] bytesIn = new byte[8192];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1)
            fOS.write(bytesIn, 0, read);

        fOS.close();
    }

    public default void printData() {
        if (Textures.items.size() != 0)
            Main.info(Optional.of("Textures"), "Registered " + Textures.items.size() + " textured item(s).");
        if (Textures.blocks.size() != 0)
            Main.info(Optional.of("Textures"), "Registered " + Textures.blocks.size() + " textured block(s).");
        if (Textures.plugins.size() != 0) {
            Main.info(Optional.of("Textures"), "Registered plugins include:");
            for (Plugin plugin : Textures.plugins)
                Main.info(Optional.of("Textures"), plugin.getName());
        }

        Main.info(Optional.of("Textures"),
                "Registered " + (Textures.blocks.size() + Textures.items.size()) + " textured instance(s).");
    }

    public default File extractFile(Plugin plugin, String name, File where) {
        Main.info(Optional.of("Textures"), " Retrieving assets.zip out of the plugin \"" + plugin.getName() + "\".");

        try {
            FileOutputStream fOS = new FileOutputStream(where);
            byte[] resourceData = IOUtils.toByteArray(plugin.getResource(name));
            fOS.write(resourceData, 0, resourceData.length);
            fOS.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return where;
    }

    public default void deleteFile(File file) {
        if (file.exists())
            try {
                FileUtils.forceDelete(file);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
    }

    public default void printModelData(PrintWriter printWriter, Material baseMaterial) {
        List<TexturedBase> texturedList = new ArrayList<>(Textures.items.size() + Textures.blocks.size());

        Textures.blocks.forEach((block) -> texturedList.add(block));

        Textures.items.forEach((item) -> texturedList.add(item));

        if (texturedList.size() != 0)
            printWriter.write("{ \"parent\": \"item/handheld\", \"textures\": { \"layer0\": \"items/"
                    + baseMaterial.name().toLowerCase()
                    + "\" }, \"overrides\": [ { \"predicate\": {\"custom_model_data\": 0}, \"model\": \""
                    + baseMaterial.name().toLowerCase() + "\"}\"");

        texturedList.forEach((textured) -> {
            if (textured.getBaseMaterial() == baseMaterial)
                if (textured != null) {
                    Main.info(Optional.of("TEXTURES"), "Wrote custom JSON data for TexturedBase #" + textured.getId()
                            + " (" + textured.getName() + ") with id " + textured.getId());

                    printWriter.println(",{ \"predicate\": {\"custom_model_data\": " + textured.getId()
                            + "}, \"model\": \"" + textured.getModel() + "\"}");
                }
        });

        if (texturedList.size() != 0)
            printWriter.println(" ]}");

        printWriter.close();
    }

    public default void writeSounds(File file) throws IOException {
        HashMap<String, SoundData> soundsMap = new HashMap<>();

        Textures.sounds.forEach((sound) -> {
            soundsMap.put(sound.getName(), new SoundData("master", Arrays.asList(sound.getModel())));
        });

        Sounds sounds = new Sounds(soundsMap);

        FileWriter fw = new FileWriter(file);
        fw.write(sounds.getJson());
        fw.close();
    }

    public default void writeMcMeta(File file) throws IOException {
        String finalJson = Textures.GSON.toJson(new McMeta(Textures.pack_format, Textures.pack_description));

        FileWriter fW = new FileWriter(file);
        fW.write(finalJson);
        fW.close();
    }

    public default void throwEvent() {
        Bukkit.getPluginManager().callEvent(new TextureSetupEvent());
    }

    public default void download(String link, File file) {
        try {
            BufferedInputStream bIS = new BufferedInputStream(new URL(link).openStream());

            FileOutputStream fOS = new FileOutputStream(file);

            byte[] buff = new byte[32*1024];
            int len=0;
            while((len = bIS.read(buff)) > 0)
                fOS.write(buff,0,len);

            // fOS.write(bIS.readAllBytes());

            fOS.close();
            bIS.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public default File createFolder(File file) {
        if (!file.exists())
            file.mkdirs();

        return file;
    }

}
