/*******************************************************************************
 * Copyright (C) IceyLeagons(https://iceyleagons.net/) 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package net.iceyleagons.frostedengineering.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

	public static int getBurnTime(ItemStack is) {
		CraftItemStack.asNMSCopy(is).getItem();
		try {
			Class<?> craftItemStack = Reflections.getClass("{cb}.inventory.CraftItemStack");
			Class<?> furnaceClass = Reflections.getClass("{nms}.TileEntityFurnace");

			Object itemstack = Reflections
					.setAccessible(craftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class))
					.invoke(craftItemStack, is);
			Object item = Reflections
					.setAccessible(itemstack.getClass().getDeclaredMethod("getItem")).invoke(itemstack);

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
