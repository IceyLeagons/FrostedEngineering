package net.iceyleagons.frostedengineering.textures;

public enum FrostedModelArgument {

	INSTA_BREAK_1_TALL(ArgumentType.BLOCK_TYPE, "113+:seagrass;113-:fern"),
	INSTA_BREAK_2_TALL(ArgumentType.BLOCK_TYPE, "113+:gray_banner;113-:banner"),
	UNBREAKABLE(ArgumentType.BLOCK_TYPE, "113+:barrier;113-:barrier"), IN_WATER(ArgumentType.PROPERTY, "inWater:true"),
	NOT_IN_WATER(ArgumentType.PROPERTY, "inWater:false");

	public final ArgumentType type;
	public final String value;

	private FrostedModelArgument(ArgumentType type, String value) {
		this.type = type;
		this.value = value;
	}

	public static enum ArgumentType {

		PROPERTY, BLOCK_TYPE;

	}

}
