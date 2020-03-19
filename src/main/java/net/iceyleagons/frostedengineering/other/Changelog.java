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
package net.iceyleagons.frostedengineering.other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagList;
import net.minecraft.server.v1_14_R1.NBTTagString;


public class Changelog {
	
	private static ItemStack book;
	private static NBTTagList pages = new NBTTagList();
	private static List<Change> changes = new ArrayList<Change>();
	
	public static void populateBook() {
		
		try {
			changes.add(new Change(getDate("2020/02/02/17/34"),"Test",new String[] {"Added changelog"}));
			changes.add(new Change(getDate("2020/02/02/17/35"),"Test2",new String[] {"Added changelog"}));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		changes.forEach(c -> {
			String date = new SimpleDateFormat("yyyy.MM.dd hh:mm").format(c.getDate());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < c.getElements().length; i++) {
				String as = "{\"text\":\"\n\n - "+c.getElements()[i]+"\",\"color\":\"reset\"},";
				sb.append(as);
				
			}
			pages.add(new NBTTagString("[{\"text\":\""+c.getName()+"\",\"bold\":true\",\"underlined\":\"true\"},{\"text\":\"\nTime\",\"color\":\"reset\",\"italic\":\"false\",\"bold\":\"true\",\"underlined\":\"false\"},{\"text\":\": "+date+"\",\"italic\":\"true\"},"+sb.toString()+"{\"text\":\"\"}]"));
		});
		
		
		
		generateBook();
	}
	
	public static void giveToPlayer(Player p) {
		p.getInventory().addItem(book);
	}
	
	private static Date getDate(String s) throws ParseException {
		return new SimpleDateFormat("yyy/MM/dd/hh/mm").parse(s);
	}
	
	private static void generateBook() {
		book = new ItemStack(Material.WRITTEN_BOOK);
		net.minecraft.server.v1_14_R1.ItemStack b = CraftItemStack.asNMSCopy(book);
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("title", "Changelog");
		tag.setString("author", "IceyLeagons");
		tag.set("pages", pages);
		b.setTag(tag);
		book =CraftItemStack.asCraftMirror(b);
	}

}

class Change {
	
	private Date date;
	private String name;
	private String[] elements;
	
	public Change(Date d, String name, String[] elements) {
		this.date = d;
		this.name = name;
		this.elements = elements;
	}

	public Date getDate() {
		return date;
	}

	public String getName() {
		return name;
	}

	public String[] getElements() {
		return elements;
	}
	
	
	
}
