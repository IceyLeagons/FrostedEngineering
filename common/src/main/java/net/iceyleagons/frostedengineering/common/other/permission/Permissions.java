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
package net.iceyleagons.frostedengineering.common.other.permission;

public enum Permissions {

    COMMAND_DEBUG("frostedengineering.command.debug", ParentPermissionType.REGULAR),
    NO_PERM("", ParentPermissionType.REGULAR),
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
