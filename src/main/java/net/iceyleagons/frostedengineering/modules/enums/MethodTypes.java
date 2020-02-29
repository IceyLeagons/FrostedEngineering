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
package net.iceyleagons.frostedengineering.modules.enums;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author TOTHT
 *
 */
public enum MethodTypes {
	/**
	 *  This is for methods that would run when the {@link FrostedMain} finishes all of it's tasks before this
	 */
	ON_ENABLE,
	/**
	 *  This is for methods that would run when the {@link FrostedMain} shuts down.
	 */
	ON_DISABLE,
	/**
	 *  This is for methods that would run when the {@link FrostedMain#onLoad()} fires
	 */
	ON_LOAD,
	/**
	 *  This is for methods that need to be run every single game tick. They will be called by a {@link BukkitRunnable}
	 */
	ON_TICK;
}

