package net.iceyleagons.frostedengineering.textures.sound;

import java.util.HashMap;
import java.util.List;

import net.iceyleagons.frostedengineering.textures.TextureProvider;

import com.google.gson.annotations.Expose;

import org.apache.commons.lang.StringUtils;

public class Sounds {

    @Expose(deserialize = false)
    public HashMap<String, SoundData> sounds;

    private final String json;

    public String getJson() {
        return json;
    }

    public Sounds(HashMap<String, SoundData> sounds) {
        this.sounds = sounds;
        this.json = StringUtils.removeEnd(TextureProvider.GSON.toJson(this).replace("\"sounds\": {", ""), "}");
    }

    public static class SoundData {
        @Expose(deserialize = false)
        public String category;
        @Expose(deserialize = false)
        public List<String> sounds;

        public SoundData(String category, List<String> sounds) {
            this.category = category;
            this.sounds = sounds;
        }
    }
}