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

package net.iceyleagons.frostedengineering.common.computers.robots.functions;

import lombok.Getter;
import net.iceyleagons.frostedengineering.common.computers.robots.Robot;
import vm2.LuaTable;
import vm2.LuaValue;
import vm2.lib.OneArgFunction;

/**
 * @author TOTHTOMI
 */
public class RobotFunctions extends OneArgFunction {

    @Getter
    private Robot robot;

    public RobotFunctions(Robot robot) {
        this.robot = robot;
    }

    @Override
    public LuaValue call(LuaValue arg) {
        LuaTable robotFunc = new LuaTable();
        robotFunc.set("turnRight",new TurnRight(getRobot()));
        robotFunc.set("turnLeft",new TurnLeft(getRobot()));
        robotFunc.set("moveUp",new MoveUp(getRobot()));
        robotFunc.set("moveDown",new MoveDown(getRobot()));
        robotFunc.set("moveForward",new MoveForward(getRobot()));
        arg.set("robot",robotFunc);
        arg.get("package").get("loaded").set("robot",robotFunc);
        return robotFunc;
    }
}
