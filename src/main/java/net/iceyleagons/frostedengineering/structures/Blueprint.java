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

package net.iceyleagons.frostedengineering.structures;

import lombok.Data;
import net.iceyleagons.frostedengineering.gui.InventoryFactory;
import net.iceyleagons.frostedengineering.hologram.HoloAPI;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;
import net.iceyleagons.frostedengineering.utils.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author TOTHTOMI
 */
@Data
public class Blueprint implements InventoryHolder {

    private String name, description;
    private int level;
    private TexturedBlock texturedBlock;
    private List<ItemStack> ingredients;
    private InventoryFactory inventoryFactory;
    private HoloAPI holoAPI;
    private int totalAmountOfItems;

    public Blueprint(String name, String description, int level, TexturedBlock texturedBlock, List<ItemStack> ingredients) {
        setName(name);
        setDescription(description);
        setLevel(level);
        setTexturedBlock(texturedBlock);
        setIngredients(ingredients);
        totalAmountOfItems = getIngredients().stream().mapToInt(ItemStack::getAmount).sum();
        ItemStack itemStack = ItemFactory.newFactory(Material.AIR).build();
        this.inventoryFactory = new InventoryFactory(name,27,itemStack,false);

        ItemStack glass = ItemFactory.newFactory(Material.GLASS_PANE).hideAttributes().build();


        List<Integer> seperator = Arrays.asList(2,11,20,6,15,24);
        getInventoryFactory().setDenyList(Arrays.asList(2,11,20,6,15,24,7,8,16,17,25,26,13));
        getInventoryFactory().setItem(glass,13);
        seperator.forEach(integer -> getInventoryFactory().setItem(glass,integer));
    }


    private Location location;

    public void open(Player player) {
        getInventoryFactory().openInventory(player);
    }

    public void offerIngredient(ItemStack itemStack) {

    }

    public void updateHologram() {
        int input = 0;
        int toPaint = (int) Math.floor((float)(input / totalAmountOfItems * 40f));
        Bukkit.getOnlinePlayers().forEach(getHoloAPI()::destroy);

        List<String> lines = new ArrayList<>();
        lines.add("&9&lIngredients: ");
        lines.add(" ");

        StringBuilder stringBuilder = new StringBuilder();
        if (toPaint > 0) stringBuilder.append("§a"); else stringBuilder.append("§c");

        if (input == totalAmountOfItems) {
            stringBuilder.append("||||||||||||||||||||||||||||||||||||||||");
        } else {
            for (int i = 0; i < toPaint; i++) {
                stringBuilder.append("|");
            }
            stringBuilder.append("§c");
            for (int i = toPaint; i < 40; i++) {
                stringBuilder.append("|");
            }
        }
        lines.add(stringBuilder.toString());
        setHoloAPI(HoloAPI.newInstance(location,lines));
        Bukkit.getOnlinePlayers().forEach(getHoloAPI()::display);
        //holoAPI.
    }

    public void build(Location location,Player player) {
        setLocation(location);
        List<String> lines = new ArrayList<>();
        lines.add("&9&lIngredients: ");
        lines.add(" ");
        lines.add("&c||||||||||||||||||||||||||||||||||||||||");
        setHoloAPI(HoloAPI.newInstance(location,lines));
        Bukkit.getOnlinePlayers().forEach(getHoloAPI()::display);
    }

    @Override
    public Inventory getInventory() {
        return getInventoryFactory().getSourceInventory();
    }
}
