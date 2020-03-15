package net.iceyleagons.frostedengineering.textures;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;

import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;
import net.iceyleagons.frostedengineering.textures.interfaces.IWrapped;

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
		List<Object> arguments = Arrays.asList(args);

		assert arguments.get(0) instanceof Block : "first argument not block";

		Block b = (Block) arguments.get(0);

		if (arguments.get(1) instanceof String) {
			String data = (String) arguments.get(1);
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
		} else if (arguments.get(1) instanceof TexturedBlock) {
			try {
				if (b.getState() instanceof CreatureSpawner) {
					CreatureSpawner cs = (CreatureSpawner) b.getState();

					TexturedBlock data = (TexturedBlock) arguments.get(1);

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

		assert arguments.get(1) instanceof String : "second argument not supported";

		return false;
	}

}
