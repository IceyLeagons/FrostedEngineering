package net.iceyleagons.frostedengineering.textures;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Server;

public class Reflections {
	public static String craftBukkitPrefix = "org.bukkit.craftbukkit";
	public static String nmsPrefix = "net.minecraft.server";

	private static HashMap<String, Class<?>> cache = new HashMap<>();

	public static String versionString;
	public static Version version;

	static {
		if (Bukkit.getServer() != null) {
			Server server = Bukkit.getServer();
			Class<?> bukkitServerClass = server.getClass();
			String[] pas = bukkitServerClass.getName().split("\\.");
			if (pas.length == 5) {
				String verB = pas[3];
				versionString = verB;
				craftBukkitPrefix += "." + verB;
			}
			try {
				Method getHandle = bukkitServerClass.getDeclaredMethod("getHandle");
				Object handle = getHandle.invoke(server);
				Class<?> handleServerClass = handle.getClass();
				pas = handleServerClass.getName().split("\\.");
				if (pas.length == 5) {
					String verM = pas[3];
					nmsPrefix += "." + verM;
				}
			} catch (Exception ignored) {
			}
		}
		Arrays.asList(Version.values()).forEach((version2) -> {
			if(versionString.contains(version2.name().toLowerCase())) {
				version = version2;
			}
		});
	}

	private static Class<?> existsAlready(String className) {
		if (cache.containsKey(className))
			return cache.get(className);
		return null;
	}

	private static Class<?> cache(String className, Class<?> clazz) {
		cache.put(className, clazz);

		return clazz;
	}

	public static Class<?> getClass(String... classes) {
		for (String className : classes)
			try {
				Class<?> exist = existsAlready(className);
				if (exist != null)
					return exist;
				else
					return cache(className,
							Class.forName(className.replace("{cb}", craftBukkitPrefix).replace("{nms}", nmsPrefix)));
			} catch (ClassNotFoundException ignored) {
			}
		throw new RuntimeException("no class found");
	}

	public static Method setAccessible(Method f) {
		f.setAccessible(true);
		return f;
	}

	public enum Version {
		V1_15(5), V1_14(4);

		public int packFormat;

		private Version(int packFormat) {
			this.packFormat = packFormat;
		}
	}

}
