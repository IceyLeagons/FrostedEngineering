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
package net.iceyleagons.frostedengineering.entity;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.DataConverterRegistry;
import net.minecraft.server.v1_14_R1.DataConverterTypes;
import net.minecraft.server.v1_14_R1.Entity;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumCreatureType;
import net.minecraft.server.v1_14_R1.IRegistry;
import net.minecraft.server.v1_14_R1.SharedConstants;
import net.minecraft.server.v1_14_R1.WorldServer;

public class Registry {
	
	public static EntityTypes<?> CUSTOM_ZOMBIE;
	
	@SuppressWarnings("unchecked")
	public static void load() {
        Map<String, Type<?>> types = (Map<String, Type<?>>) DataConverterRegistry.a().getSchema(DataFixUtils.makeKey(SharedConstants.a().getWorldVersion())).findChoiceType(DataConverterTypes.ENTITY).types();
        types.put("minecraft:" + "customzombie", types.get("minecraft:zombie"));
        EntityTypes.a<Entity> a = EntityTypes.a.a(CustomZombie::new, EnumCreatureType.MONSTER);
        CUSTOM_ZOMBIE = IRegistry.a(IRegistry.ENTITY_TYPE, "customzombie", a.a("customzombie"));
	}

	public static void spawnMob(EntityTypes<?> t,Location loc) {
		WorldServer w = ((CraftWorld) loc.getWorld()).getHandle();
		Entity e = t.b(w, null, null, null, new BlockPosition(loc.getX(),loc.getY(),loc.getZ()), null, false, false);
		w.addEntity(e);
	}
	
}
	
