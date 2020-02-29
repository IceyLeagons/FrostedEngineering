package net.iceyleagons.frostedengineering.other.permission;

import org.bukkit.command.CommandSender;

public class PermissionManager {

	public static boolean hasPermission(CommandSender s, Permissions permission) {
		if (s.hasPermission(permission.getPermission()))
			return true;
		else if (s.hasPermission(permission.getParentPermissionType().getPermission()))
			return true;

		return false;
	}

}
