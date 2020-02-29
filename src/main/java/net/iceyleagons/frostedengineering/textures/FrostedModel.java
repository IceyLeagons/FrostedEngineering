package net.iceyleagons.frostedengineering.textures;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.gson.Gson;

import fastnoise.MathUtils.Vector3;

// TODO: FINISH THIS!!! kinda urgent dunno lmao

public class FrostedModel {

	public static HashMap<FrostedModel, ItemStack> items = new HashMap<>();
	@SuppressWarnings("unused")
	private static Gson gson = new Gson();

	ItemStack item = new ItemStack(Material.BARRIER);
	final FrostedModelArgument[] args;

	public ItemStack getItem() {
		return item;
	}

	public void generateJson() {
		// TODO: Implement this
	}

	final Vector3 position, rotation, scale;
	final boolean usePosition, useRotation;
	final String texture;

	public static FrostedModel createFrostedModel(Vector3 position, Vector3 rotation, String texture) {
		return new FrostedModel(position, rotation, texture);
	}

	public static FrostedModel createFrostedModel(Vector3 position, Vector3 rotation, Vector3 scale, String texture) {
		return new FrostedModel(position, rotation, scale, texture);
	}

	private FrostedModel(Vector3 position, Vector3 rotation, String texture, FrostedModelArgument... arguments) {
		this.position = position;
		this.rotation = rotation;
		this.scale = new Vector3(1.f, 1.f, 1.f);
		this.usePosition = true;
		this.useRotation = true;
		this.texture = texture;
		this.args = arguments;
	}

	private FrostedModel(Vector3 position, Vector3 rotation, Vector3 scale, String texture,
			FrostedModelArgument... arguments) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.usePosition = true;
		this.useRotation = true;
		this.texture = texture;
		this.args = arguments;
	}

	private FrostedModel(String texture, FrostedModelArgument... arguments) {
		this.position = null;
		this.rotation = null;
		this.scale = new Vector3(1.f, 1.f, 1.f);
		this.usePosition = false;
		this.useRotation = false;
		this.texture = texture;
		this.args = arguments;
	}

}
