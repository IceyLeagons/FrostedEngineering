package net.iceyleagons.frostedengineering.network.energy.components.sub.consumers.furnace;

import net.iceyleagons.frostedengineering.Main;
import net.iceyleagons.frostedengineering.network.energy.components.sub.ComponentManager;
import net.iceyleagons.frostedengineering.textures.base.TexturedBlock;

public class TexturedFurnace extends TexturedBlock {

    public TexturedFurnace() {
        super(Main.MAIN, "eletric_furnace", "block/electric_furnace", "§rElectric furnace");
        ComponentManager.registerComponent("fe:electricfurnace", this);
    }

}