package net.iceyleagons.frostedengineering.api.addon;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.iceyleagons.frostedengineering.api.exceptions.addon.MalformedAddonMetadataException;
import net.iceyleagons.icicle.reflect.Reflections;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Scanner;

/**
 * Contains necessary and optional information about an addon.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.0.0
 */
@EqualsAndHashCode
public class AddonMetadata {

    /**
     * <b>Required</b>
     *
     * @return the name of the addon
     */
    @Getter
    private String name;

    /**
     * <b>Required</b>
     *
     * @return the path to the main class of the addon
     */
    @Getter
    private String main;

    /**
     * @return the name of the addon's authors
     */
    @Getter
    private String[] authors;

    /**
     * <b>Dependencies are not yet implemented in loading!</b>
     *
     * @return the dependencies of the addon
     */
    @Getter
    private String[] dependencies;

    /**
     * @return the website of the addon
     */
    @Getter
    private String website;

    /**
     * <b>Required</b>
     *
     * @return the version of the addon
     */
    @Getter
    private String version;

    /**
     * <b>Required</b>
     *
     * @return the description of the addon
     */
    @Getter
    private String description;


    /**
     * Will attempt to create an addon metadata, if an error occurs see the exceptions down below.
     *
     * @param inputStream the input stream of the metadata file
     * @param file the addon jar file itself
     * @throws IOException if the {@link InputStream#close()} returned an error
     * @throws MalformedAddonMetadataException if the addon.json does not contain required fields
     * @throws IllegalAccessException if the class fields can not be accessed by {@link Reflections} (internal error)
     */
    public AddonMetadata(InputStream inputStream, File file) throws IOException, MalformedAddonMetadataException, IllegalAccessException {
        JSONObject jsonObject = getJSONObject(inputStream);
        inputStream.close();

        handleField("name", jsonObject, file, false, true);
        handleField("main", jsonObject, file, false, true);

        handleField("authors", jsonObject, file, true, true);
        handleField("dependencies", jsonObject, file, true, false);

        handleField("website", jsonObject, file, false, false);
        handleField("version", jsonObject, file, false, true);
        handleField("description", jsonObject, file, false, true);
    }

    /**
     * Checks whether the jsonObject contains a field named with the given name if so then if arrayExpected is true it will
     * try to parse it as an array and fills in the appropriate field in this class, otherwise if it does not contain it and it's required
     * it will throw an error, otherwise no.
     *
     * @param name the name of the field
     * @param jsonObject the {@link JSONObject}
     * @param addonFile the addon {@link File}
     * @param arrayExpected whether it's an array or not
     * @param required whether it's required or not
     * @throws MalformedAddonMetadataException if the metadata does not contain a required field
     * @throws IllegalAccessException if the field in this jar can not be accessed
     */
    private void handleField(String name, JSONObject jsonObject, File addonFile, boolean arrayExpected, boolean required) throws MalformedAddonMetadataException, IllegalAccessException {
        if (jsonObject.has(name)) {
            Field field = Reflections.getField(getClass(), name, true);
            if (arrayExpected) {
                JSONArray jsonArray = jsonObject.getJSONArray(name);
                String[] strings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    Object o = jsonArray.get(i);
                    if (o instanceof String)
                        strings[i] = (String) o;
                }
                field.set(this, strings);
                return;
            }

            Object o = jsonObject.get(name);
            if (o instanceof String) {
                String string = (String) o;
                field.set(this, string);
            }

        } else if (required) throw new MalformedAddonMetadataException(addonFile);
    }

    /**
     * Reads in the file and parses it into a {@link JSONObject}
     *
     * @param inputStream the {@link InputStream}
     * @return the {@link JSONObject}
     */
    private JSONObject getJSONObject(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream)) {
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNext()) stringBuilder.append(scanner.next());

            return new JSONObject(stringBuilder.toString());
        }
    }

}
