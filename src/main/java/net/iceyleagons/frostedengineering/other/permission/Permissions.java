package net.iceyleagons.frostedengineering.other.permission;

public enum Permissions {
	
	COMMAND_DEBUG("frostedengineering.command.debug", ParentPermissionType.REGULAR),
	NO_PERM("",ParentPermissionType.REGULAR),
	COMMAND_SAVESTRUCT("frostedengineering.command.savestruct", ParentPermissionType.ADMIN),
	COMMAND_GIVE("frostedengineering.command.give", ParentPermissionType.ADMIN);
	
	String perm;
	ParentPermissionType ppt;
	
	Permissions(String perm, ParentPermissionType ppt) {
		this.perm = perm;
		this.ppt = ppt;
	}
	
	public String getPermission() {
		return this.perm;
	}
	
	public ParentPermissionType getParentPermissionType() {
		return this.ppt;
	}
	
}

enum ParentPermissionType {
	REGULAR("frostedengineering.user.regular"), MODERATOR("frostedengineering.user.moderator"), ADMIN("frostedengineering.user.admin");
	
	String perm;
	
	ParentPermissionType(String perm) {
		this.perm = perm;
	}
	
	public String getPermission() {
		return this.perm;
	}
}
