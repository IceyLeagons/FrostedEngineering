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
package net.iceyleagons.frostedengineering.common.gui.installer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.iceyleagons.frostedengineering.common.Main;
import net.iceyleagons.frostedengineering.common.storage.StorageHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class Install {

    List<Step> steps = new ArrayList<Step>();
    Iterator<Step> stepIterator;
    int i = 0;
    Player p;

    public Install(Player p) {
        this.p = p;


        steps.add(new Step(StepType.PLAYER_INPUT, "Language (2 letter country code)", new String[]{}, new AnvilGUIRunnable() {

            @Override
            public void run(String text) {
                StorageHandler.getYamlHandler().getConfig("config").getConfig().set("lang", text);
                StorageHandler.getYamlHandler().getConfig("config").saveConfig();
                nextStep();
            }
        }, p).build());

        steps.add(new Step(StepType.YES_OR_NO, "Auto update?", new String[]{"Would you like to enable auto update?"}, () -> {
            StorageHandler.getYamlHandler().getConfig("config").getConfig().set("auto-update", true);
            StorageHandler.getYamlHandler().getConfig("config").saveConfig();
            nextStep();
        }, () -> {
            StorageHandler.getYamlHandler().getConfig("config").getConfig().set("auto-update", false);
            StorageHandler.getYamlHandler().getConfig("config").saveConfig();
            nextStep();
        }, p).build());
        steps.add(new Step(StepType.YES_OR_NO, "Cheating?", new String[]{"Would you like to enable cheating?"}, () -> {
            StorageHandler.getYamlHandler().getConfig("config").getConfig().set("allow-cheating", true);
            StorageHandler.getYamlHandler().getConfig("config").saveConfig();
            nextStep();
        }, () -> {
            StorageHandler.getYamlHandler().getConfig("config").getConfig().set("allow-cheating", false);
            StorageHandler.getYamlHandler().getConfig("config").saveConfig();
            nextStep();
        }, p).build());

        stepIterator = steps.listIterator();
    }

    public void start() {
        p.sendTitle("§b§lPlease wait...", "§aThe installer will walk you through the steps", 20, 60, 20);
        Bukkit.getScheduler().runTaskLater(Main.MAIN, () -> {
            nextStep();
        }, 40L);
    }

    public void nextStep() {
        if (stepIterator.hasNext()) {
            if (p.getOpenInventory() != null) p.closeInventory();
            Bukkit.getScheduler().runTaskLater(Main.MAIN, () -> {
                stepIterator.next().show();
            }, 20L);
        }
    }
}
