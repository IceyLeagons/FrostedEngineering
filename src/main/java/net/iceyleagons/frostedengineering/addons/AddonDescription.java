package net.iceyleagons.frostedengineering.addons;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class AddonDescription {

    private String name, version, description, main, authors;

    public AddonDescription(InputStream is) throws UnsupportedEncodingException, IOException, ParseException {
        JSONObject obj = getJSONObject(is);
        name = obj.getString("name");
        version = obj.getString("version");
        description = obj.getString("description");
        main = obj.getString("main");
        authors = obj.getString("authors");
    }

    private JSONObject getJSONObject(InputStream is) {
        JSONParser parser = new JSONParser();
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(is, "UTF-8");
            org.json.simple.JSONObject simpleobj = (org.json.simple.JSONObject) parser.parse(isr);
            JSONObject obj = new JSONObject(simpleobj.toJSONString());
        } catch (IOException | ParseException ex) {

        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ignore) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignore) {
                }
            }
        }
        return null;
    }

    /**
     * @return the name of the addon, defined in the addon.json file
     */
    public String getName() {
        return name;
    }

    /**
     * @return the version of the addon, defined in the addon.json file
     */
    public String getVersion() {
        return version;
    }

    /**
     * @return the description of the addon, defined in the addon.json file
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the path to the main class of the addon, defined in the addon.json
     * file
     */
    public String getMain() {
        return main;
    }

    /**
     * @return the authors of the addon, defined in the addon.json file
     */
    public String getAuthors() {
        return authors;
    }

}
