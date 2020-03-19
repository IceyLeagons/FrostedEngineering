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
package net.iceyleagons.frostedengineering.textures;

import java.lang.reflect.Field;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;
import net.iceyleagons.frostedengineering.textures.interfaces.IWrapped;
import net.iceyleagons.frostedengineering.utils.Reflections;

public class WrappedMobSpawner implements IWrapped {

	private static Class<?> cCSClass;
	private static Class<?> mSAClass;
	private static Class<?> mojangsonClass;

	static {
		try {
			cCSClass = Reflections.getClass("{cb}.block.CraftCreatureSpawner");
			mSAClass = Reflections.getClass("{nms}.MobSpawnerAbstract");
			mojangsonClass = Reflections.getClass("{nms}.MojangsonParser");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public boolean execute(Object... args) {
		assert args[0] instanceof Block : "first argument not block";

		Block b = (Block) args[0];

		if (args[1] instanceof String) {
			String data = (String) args[1];
			try {
				if (b.getState() instanceof CreatureSpawner) {
					CreatureSpawner cs = (CreatureSpawner) b.getState();
					cs.setSpawnedType(EntityType.ARMOR_STAND);

					Object tileEntity = Reflections
							.setAccessible(cCSClass.getSuperclass().getDeclaredMethod("getTileEntity")).invoke(cs);
					Object mobSpawnerAbstract = Reflections
							.setAccessible(tileEntity.getClass().getDeclaredMethod("getSpawner")).invoke(tileEntity);

					Object nbt = Reflections.setAccessible(mojangsonClass.getMethod("parse", String.class)).invoke(null,
							data);
					Reflections.setAccessible(mSAClass.getDeclaredMethod("a", nbt.getClass()))
							.invoke(mobSpawnerAbstract, nbt);

					return true;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else if (args[1] instanceof TexturedBlock) {
			try {
				if (b.getState() instanceof CreatureSpawner) {
					CreatureSpawner cs = (CreatureSpawner) b.getState();

					TexturedBlock data = (TexturedBlock) args[1];

					Object tileEntity = Reflections
							.setAccessible(cCSClass.getSuperclass().getDeclaredMethod("getTileEntity")).invoke(cs);
					Object mobSpawnerAbstract = Reflections
							.setAccessible(tileEntity.getClass().getDeclaredMethod("getSpawner")).invoke(tileEntity);
					Field spawnDataField = mSAClass.getDeclaredField("spawnData");

					spawnDataField.setAccessible(true);
					Object mobSpawnerData = spawnDataField.get(mobSpawnerAbstract);
					Object nbtTagCompound = Reflections
							.setAccessible(mobSpawnerData.getClass().getDeclaredMethod("getEntity"))
							.invoke(mobSpawnerData); // Might be "b" in 1.14.
					Object nbtList = Reflections.setAccessible(nbtTagCompound.getClass().getMethod("get", String.class))
							.invoke(nbtTagCompound, "ArmorItems");
					Object itemNBTTagCompound = Reflections
							.setAccessible(nbtList.getClass().getDeclaredMethod("get", int.class)).invoke(nbtList, 3);
					String itemType = (String) Reflections
							.setAccessible(itemNBTTagCompound.getClass().getMethod("getString", String.class))
							.invoke(itemNBTTagCompound, "id");
					short damage = (short) Reflections
							.setAccessible(itemNBTTagCompound.getClass().getMethod("getShort", String.class))
							.invoke(itemNBTTagCompound, "Damage");
					if (itemType.contains(data.getBaseMaterial().name().toLowerCase())) {
						if (damage == data.getId()) {
							return true;
						}
					}
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		assert args[1] instanceof String : "second argument not supported";

		return false;
	}

}
