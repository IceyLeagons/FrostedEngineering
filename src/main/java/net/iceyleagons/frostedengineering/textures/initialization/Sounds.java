package net.iceyleagons.frostedengineering.textures.initialization;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.annotations.Expose;

import net.iceyleagons.frostedengineering.textures.Textures;

public class Sounds {

	@Expose(serialize = true, deserialize = false)
	public HashMap<String, SoundData> sounds = new HashMap<>();

	private String json;

	public String getJson() {
		return json;
	}

	public Sounds(HashMap<String, SoundData> sounds) {
		this.sounds = sounds;
		this.json = StringUtils.removeEnd(Textures.GSON.toJson(this).replace("\"sounds\": {", ""), "}");
	}

	public static class SoundData {
		@Expose(serialize = true, deserialize = false)
		public String category = "master";
		@Expose(serialize = true, deserialize = false)
		public List<String> sounds;

		public SoundData(String category, List<String> sounds) {
			this.category = category;
			this.sounds = sounds;
		}
	}
}
