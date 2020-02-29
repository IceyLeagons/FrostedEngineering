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
package net.iceyleagons.frostedengineering.modules.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author TOTHT
 *
 * This needs to be declared on top of the class!
 * This is the heart of this system, without it won't work!
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Module {
	
	/**
	 * @return the name of the module
	 */
	String name();
	/**
	 * @return the description of the module. Every element of the array is a new line in the GUI!
	 */
	String[] description();
	/**
	 * @return the authors of the module.
	 */
	String[] authors();

}
