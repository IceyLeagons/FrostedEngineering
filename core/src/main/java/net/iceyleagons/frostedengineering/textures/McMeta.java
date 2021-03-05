package net.iceyleagons.frostedengineering.textures;

import com.google.gson.annotations.Expose;

public class McMeta {
    @Expose(deserialize = false)
    public Pack pack;

    public McMeta(int pack_format, String description) {
        this.pack = new Pack(pack_format, description);
    }

    public static class Pack {
        @Expose(deserialize = false)
        public int pack_format;
        @Expose(deserialize = false)
        public String description;

        public Pack(int pack, String desc) {
            this.pack_format = pack;
            this.description = desc;
        }
    }
}