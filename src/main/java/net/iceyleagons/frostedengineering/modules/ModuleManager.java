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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.inventory.Inventory;

import javassist.NotFoundException;
import net.iceyleagons.frostedengineering.modules.annotations.Module;
import net.iceyleagons.frostedengineering.modules.annotations.ModuleMethods;
import net.iceyleagons.frostedengineering.modules.annotations.ModuleReturns;
import net.iceyleagons.frostedengineering.modules.enums.MethodTypes;

/**
 * @author TOTHTOMI
 * <p>
 * ("I suffered brain damage while I was trying to fix the errors&bugs of this class" - One of the Last words of TOTHTOMI --> Try to find more :))
 * This class handles and contains all of the modules, that get registered through our API or by us.
 */
public class ModuleManager {

    public static List<ModuleClass> modules = new ArrayList<ModuleClass>();

    /**
     * This will go through the class and check for annotations if founds one it will fill out the proper fields of {@link ModuleClass}
     *
     * @param obj is the Object where the module is located (Class)
     */
    public void registerModule(@Nonnull Object obj) {
        Class<?> clazz = obj.getClass();
        //System.out.println("Registering?");
        if (clazz.isAnnotationPresent(Module.class)) {
            //System.out.println("passed");
            Module m = clazz.getAnnotation(Module.class);
            //System.out.println("Registering module " + m.name());
            ModuleClass mc = new ModuleClass(m.name(), m.description(), m.authors());
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(ModuleMethods.class)) {
                    ModuleMethods mm = method.getAnnotation(ModuleMethods.class);
                    mc.addMethod(method, obj, mm.type());
                }
            }
            try {
                for (PropertyDescriptor propertyDescriptor :
                        Introspector.getBeanInfo(clazz).getPropertyDescriptors()) {

                    if (propertyDescriptor.getReadMethod().isAnnotationPresent(ModuleReturns.class)) {
                        ModuleReturns mr = propertyDescriptor.getReadMethod().getAnnotation(ModuleReturns.class);
                        switch (mr.type()) {
                            case SETTINGS_INVENTORY:
                                mc.setSettingInventory((Inventory) propertyDescriptor.getReadMethod().invoke(obj, new Object[]{}));
                                break;
                            default:
                                break;

                        }
                    }
                }
            } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            modules.add(mc);
        }
    }

    /**
     * This method is called when the plugin {@link FrostedMain} loaded&registered all the modules, and they're ready to be enabled
     * It will invoke the methods with the {@link MethodTypes#ON_ENABLE} annotation type on them
     */
    public void enableModules() {
        modules.forEach(m -> {
            try {
                enableModule(m.getName());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * This method is called when the plguin {@link FrostedMain} shuts down. It will invoke the methods with the {@link MethodTypes#ON_DISABLE} annotation type on them
     */
    public void disableModules() {
        modules.forEach(m -> {
            try {
                disableModule(m.getName());
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });
        modules.clear(); //Cleanup
    }

    /**
     * This method can be used to enable modules individually.
     * It will invoke the methods with the {@link MethodTypes#ON_ENABLE} annotation type on them
     *
     * @param name is the name of the module
     * @throws NotFoundException if there isn't any modules that has the set name.
     */
    public void enableModule(@Nonnull String name) throws NotFoundException {
        ModuleClass mc = getModule(name);
        if (mc != null) {
            System.out.println("Invoking");
            mc.invokeMethods(MethodTypes.ON_ENABLE);
        } else {
            throw new NotFoundException("There isn't any Module called " + name);
        }
    }

    /**
     * This method can be used to disable modules individually.
     * It will invoke the methods with the {@link MethodTypes#ON_DISABLE} annotation type on them
     *
     * @param name is the name of the module
     * @throws NotFoundException if there isn't any modules that has the set name.
     */
    public void disableModule(@Nonnull String name) throws NotFoundException {
        ModuleClass mc = getModule(name);
        if (mc != null) {
            mc.invokeMethods(MethodTypes.ON_DISABLE);
        } else {
            throw new NotFoundException("There isn't any Module called " + name);
        }
    }

    /**
     * This method can be used to gather the ModuleClass by using the module name
     *
     * @param name is the name of the module
     * @return the found ModuleClass (CAN BE NULL, SO WRITE A CHECK FOR IT)
     */
    public ModuleClass getModule(@Nonnull String name) {
        for (ModuleClass module : modules) {
            if (module.getName().equalsIgnoreCase(name)) return module;
        }
        return null;
    }
}
