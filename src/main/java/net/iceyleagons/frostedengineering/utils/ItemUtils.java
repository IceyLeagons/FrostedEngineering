/*
 *  Copyright (C) IceyLeagons(https://iceyleagons.net/)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.iceyleagons.frostedengineering.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ItemUtils {

    private static Gson gson;

    static {
        gson = new GsonBuilder().create();
    }

    public static int getBurnTime(ItemStack is) {
        try {
            Class<?> craftItemStack = Reflections.getClass("{cb}.inventory.CraftItemStack");
            Class<?> furnaceClass = Reflections.getClass("{nms}.TileEntityFurnace");

            Object itemstack = Reflections
                    .setAccessible(craftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class))
                    .invoke(craftItemStack, is);
            Object item = Reflections.setAccessible(itemstack.getClass().getDeclaredMethod("getItem"))
                    .invoke(itemstack);

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

    /**
     * Gets an item back from the Map created by {@link serialize()}
     *
     * @param map The map to deserialize from.
     * @return The deserialized item.
     * @throws IllegalAccessException    Things can go wrong.
     * @throws IllegalArgumentException  Things can go wrong.
     * @throws InvocationTargetException Things can go wrong.
     */
    public static ItemStack deserialize(Map<String, Object> map)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ItemStack i = ItemStack.deserialize(map);
        if (map.containsKey("meta")) {
            try {
                // org.bukkit.craftbukkit.v1_8_R3.CraftMetaItem$SerializableMeta
                // CraftMetaItem.SerializableMeta.deserialize(Map<String, Object>)
                if (ITEM_META_DESERIALIZATOR != null) {
                    ItemMeta im = (ItemMeta) DESERIALIZE.invoke(i, map.get("meta"));
                    i.setItemMeta(im);
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw e;
            }
        }
        return i;
    }

    /**
     * Serializes an ItemStack and it's ItemMeta, use {@link deserialize()} to get
     * the item back.
     *
     * @param item Item to serialize
     * @return A HashMap with the serialized item
     */
    public static Map<String, Object> serialize(ItemStack item) {
        HashMap<String, Object> itemDocument = new HashMap<String, Object>(item.serialize());
        if (item.hasItemMeta()) {
            itemDocument.put("meta", new HashMap<Object, Object>(item.getItemMeta().serialize()));
        }
        return itemDocument;
    }

    /**
     * Serializes an ItemStack and it's ItemMeta, use
     * {@link deserializeFromString()} to get the item back.
     *
     * @param item Item to serialize
     * @return A String with the serialized item
     */
    public static String serializeToString(ItemStack item) {
        Map<String, Object> map = serialize(item);
        gson.toJson(map, map.getClass());
        return gson.toJson(map);
    }

    /**
     * Gets an item back from the Map created by {@link serialize()}
     *
     * @param serialized the string to deserialize from.
     * @return The deserialized item.
     * @throws IllegalAccessException    Things can go wrong.
     * @throws IllegalArgumentException  Things can go wrong.
     * @throws InvocationTargetException Things can go wrong.
     */
    public static ItemStack deserializeFromString(String serialized) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        TypeToken<Map<String, Object>> hashMapType = new TypeToken<Map<String, Object>>() {

            /**
             *
             */
            private static final long serialVersionUID = 1L;
        };

        Map<String, Object> map = gson.fromJson(serialized.replaceAll("\\s", ""), hashMapType.getType());
        return deserialize(map);
    }

    /*
     * @return The string used in the CraftBukkit package for the version.
     */
    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        String version = name.substring(name.lastIndexOf('.') + 1) + ".";
        return version;
    }

    /**
     * Basic reflection.
     *
     * @param className hte class to search for
     * @return the class with that name.
     */
    public static Class<?> getOBCClass(String className) {
        String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    private static final Class<?> ITEM_META_DESERIALIZATOR = getOBCClass("inventory.CraftMetaItem").getClasses()[0];
    private static final Method DESERIALIZE = getDeserialize();

    private static Method getDeserialize() {
        try {
            return ITEM_META_DESERIALIZATOR.getMethod("deserialize", new Class[]{Map.class});
        } catch (NoSuchMethodException | SecurityException ex) {
            return null;
        }
    }

}
