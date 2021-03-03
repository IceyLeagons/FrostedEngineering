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
package net.iceyleagons.frostedengineering.modules;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import org.bukkit.inventory.Inventory;

import net.iceyleagons.frostedengineering.modules.enums.MethodTypes;

/**
 * @author TOTHT
 * <p>
 * This class will contain all the information about a module.
 * It will be set up by the {@link ModuleManager} using Annotations
 * <p>
 * THIS WHOLE SYSTEM IS DESIGN TO BE USED WITH ANNOTATIONS OTHERWISE IT WON'T WORK
 */
public class ModuleClass {

    private String name;

    private String[] authors, description;

    private List<Entry<Method, Object>> onEnables = new ArrayList<Entry<Method, Object>>();

    private List<Entry<Method, Object>> onDisables = new ArrayList<Entry<Method, Object>>();

    private List<Entry<Method, Object>> onTicks = new ArrayList<Entry<Method, Object>>();

    private List<Entry<Method, Object>> onLoads = new ArrayList<Entry<Method, Object>>();

    private Inventory settingInventory;

    /**
     * @param name        is the name of the module
     * @param description is the description of the module (It is a String array!)
     * @param authors     is the author(s) of the module (It is a String array!)
     */
    public ModuleClass(@Nonnull String name, String[] description, String[] authors) {
        this.name = name;
        this.description = description;
        this.authors = authors;

    }

    /**
     * This method will add the method to it's proper ArrayList
     *
     * @param m is the method
     * @param o is the object, that contains the method (basically the class)
     * @param t is the type of the method
     */
    public void addMethod(@Nonnull Method m, @Nonnull Object o, @Nonnull MethodTypes t) {
        if (m == null) throw new IllegalArgumentException("Method cannot be null!");
        if (t == null) throw new IllegalArgumentException("MethodTypes cannot be null!");
        switch (t) {
            case ON_DISABLE:
                onDisables.add(new AbstractMap.SimpleEntry<Method, Object>(m, o));
                break;
            case ON_ENABLE:
                //onEnables.add(m);
                onEnables.add(new AbstractMap.SimpleEntry<Method, Object>(m, o));
                break;
            case ON_LOAD:
                onLoads.add(new AbstractMap.SimpleEntry<Method, Object>(m, o));
                break;
            case ON_TICK:
                onTicks.add(new AbstractMap.SimpleEntry<Method, Object>(m, o));
                break;
            default:
                throw new IllegalArgumentException("Not valid MethodTypes given.");
        }
    }

    /**
     * This method will invoke all the methods in a MethodType
     *
     * @param t is the MethodType whose methods will be invoked.
     */
    public void invokeMethods(@Nonnull MethodTypes t) {
        if (t == null) throw new IllegalArgumentException("MethodTypes cannot be null!");
        switch (t) {
            case ON_DISABLE:
                for (Entry<Method, Object> m : onDisables) {
                    try {
                        m.getKey().invoke(m.getValue(), new Object[]{});
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case ON_ENABLE:
                for (Entry<Method, Object> m : onEnables) {
                    try {
                        m.getKey().invoke(m.getValue(), new Object[]{});
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case ON_LOAD:
                for (Entry<Method, Object> m : onLoads) {
                    try {
                        m.getKey().invoke(m.getValue(), new Object[]{});
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            case ON_TICK:
                for (Entry<Method, Object> m : onTicks) {
                    try {
                        m.getKey().invoke(m.getValue(), new Object[]{});
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                break;
            default:
                throw new IllegalArgumentException("Not valid MethodTypes given.");
        }
    }

    public Inventory getSettingInventory() {
        return settingInventory;
    }

    public void setSettingInventory(Inventory settingInventory) {
        this.settingInventory = settingInventory;
    }

    public String getName() {
        return name;
    }

    public String[] getAuthors() {
        return authors;
    }

    public String[] getDescription() {
        return description;
    }

    public List<Entry<Method, Object>> getOnEnables() {
        return onEnables;
    }

    public List<Entry<Method, Object>> getOnDisables() {
        return onDisables;
    }

    public List<Entry<Method, Object>> getOnTicks() {
        return onTicks;
    }

    public List<Entry<Method, Object>> getOnLoads() {
        return onLoads;
    }


}
