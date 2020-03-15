package net.iceyleagons.frostedengineering.textures.initialization;

import com.google.gson.annotations.Expose;

public class McMeta {
	@Expose(serialize = true, deserialize = false)
	public Pack pack;

	public McMeta(int pack_format, String description) {
		this.pack = new Pack(pack_format, description);
	}

	public class Pack {
		@Expose(serialize = true, deserialize = false)
		public int pack_format;
		@Expose(serialize = true, deserialize = false)
		public String description;

		public Pack(int pack, String desc) {
			this.pack_format = pack;
			this.description = desc;
		}
	}
}
