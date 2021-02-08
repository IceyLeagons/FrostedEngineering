package net.iceyleagons.computercraft.web;

import net.iceyleagons.icicle.web.Bytebin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author TOTHTOMI
 */
public class WebIDE {

    private static Bytebin bytebin;

    public static void init(JavaPlugin javaPlugin) {
        bytebin = new Bytebin(javaPlugin, "https://bytebin.lucko.me/");
    }

    public static void luaFileFromBytebin(String bytebinKey) {
        String data = bytebin.readContent(bytebinKey);
        try (PrintWriter writer = new PrintWriter(new FileWriter(bytebinKey+".net.iceyleagons.computercraft.lua", true))) {
            writer.print(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
