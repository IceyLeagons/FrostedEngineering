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

package net.iceyleagons.frostedengineering.common.computers.robots;

import net.iceyleagons.frostedengineering.common.computers.lua.LuaRunner;
import net.iceyleagons.frostedengineering.common.computers.robots.functions.RobotFunctions;
import vm2.Globals;
import vm2.LuaValue;

/**
 * @author TOTHTOMI
 */
public abstract class Robot extends LuaRunner {

    public Robot() {

    }

    public LuaValue loadCode(String code) {
        Globals globals = super.getGlobals();
        globals.load(new RobotFunctions(this));
        return super.loadCode(code,globals);
    }

    /**
     * Turns the robot to the right times amount of times.
     *
     * @param times amount of turns
     * @return true if the robot successfully moved otherwise false
     */
    public boolean turnRight(int times) {
        return false;
    }

    /**
     * Turns the robot to the left times amount of times.
     *
     * @param times amount of turns
     * @return true if the robot successfully moved otherwise false
     */
    public boolean turnLeft(int times) {
        return false;
    }

    /**
     * Moves the robot forward x amount of blocks.
     *
     * @param blocks amount of blocks to travel
     * @return true if the robot successfully moved otherwise false
     */
    public boolean moveForward(int blocks) {
        return false;
    }

    /**
     * Moves the robot upwards x amount of blocks.
     *
     * @param blocks amount of blocks to travel
     * @return true if the robot successfully moved otherwise false
     */
    public boolean moveUp(int blocks) {
        return false;
    }

    /**
     * Moves the robot downwards x amount of blocks.
     *
     * @param blocks amount of blocks to travel
     * @return true if the robot successfully moved otherwise false
     */
    public boolean moveDown(int blocks) {
        return false;
    }

}
