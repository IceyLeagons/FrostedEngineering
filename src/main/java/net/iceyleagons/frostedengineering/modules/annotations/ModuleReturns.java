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
package net.iceyleagons.frostedengineering.modules.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import net.iceyleagons.frostedengineering.modules.enums.ReturnTypes;

/**
 * @author TOTHT
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface ModuleReturns {

    /**
     * This is needed for the plugin to know what kind of information this will return!
     * !WARNING! One {@link ReturnTypes} can be only used once.
     *
     * @return the {@link ReturnTypes} for the plugin
     */
    ReturnTypes type();

}
