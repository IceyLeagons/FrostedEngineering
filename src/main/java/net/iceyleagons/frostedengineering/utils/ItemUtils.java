package net.iceyleagons.frostedengineering.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import net.iceyleagons.frostedengineering.textures.Reflections;

public class ItemUtils {

	public static int getBurnTime(ItemStack is) {
		try {
			Class<?> craftItemStack = Reflections.getClass("{cb}.inventory.CraftItemStack");
			Class<?> furnaceClass = Reflections.getClass("{nms}.TileEntityFurnace");

			Object item = Reflections
					.setAccessible(craftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class))
					.invoke(craftItemStack, is);

			@SuppressWarnings("unchecked")
			Map<Object, Integer> burnTimes = (Map<Object, Integer>) Reflections
					.setAccessible(furnaceClass.getDeclaredMethod("f")).invoke(null);

			if (burnTimes.containsKey(item)) {
				return burnTimes.get(item);
			}

		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}

		return 0;
	}

}
