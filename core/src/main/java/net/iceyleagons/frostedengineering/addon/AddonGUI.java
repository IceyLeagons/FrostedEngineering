package net.iceyleagons.frostedengineering.addon;

import net.iceyleagons.frostedengineering.api.IFrostedEngineering;
import net.iceyleagons.frostedengineering.api.addon.Addon;
import net.iceyleagons.frostedengineering.api.addon.AddonMetadata;
import net.iceyleagons.icicle.item.InventoryUtils;
import net.iceyleagons.icicle.item.ItemFactory;
import net.iceyleagons.icicle.localization.Translatable;
import net.iceyleagons.icicle.ui.GUI;
import net.iceyleagons.icicle.ui.components.impl.Button;
import net.iceyleagons.icicle.ui.components.impl.buttons.SimpleButton;
import net.iceyleagons.icicle.ui.frame.Frame;
import net.iceyleagons.icicle.ui.guis.BasePaginatedGUI;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author TOTHTOMI
 */
@GUI(
        title = "Addons",
        type = InventoryType.CHEST,
        autoUpdate = false,
        height = 6
)
public class AddonGUI extends BasePaginatedGUI implements Translatable {

    private final IFrostedEngineering iFrostedEngineering;
    private final List<Map.Entry<Addon, SimpleButton>> entries = new ArrayList<>();

    public AddonGUI(IFrostedEngineering iFrostedEngineering) {
        super();
        this.iFrostedEngineering = iFrostedEngineering;
        populate();
        update();
    }

    @Override
    public void update() {
        entries.forEach(entry -> {
            Addon addon = entry.getKey();
            SimpleButton simpleButton = entry.getValue();

            simpleButton.setPlaceholder(getAddonItemStack(addon));
        });
        super.update();
    }

    @Override
    public void openForPlayers(Player... players) {
        update();
        super.openForPlayers(players);
    }

    public void populate() {
        if (iFrostedEngineering.getAddonManager().getLoadedAddons().isEmpty()) {
            super.addFrames(0, new Frame());
        }
        Iterator<Addon> addonIterator = iFrostedEngineering.getAddonManager().getLoadedAddons().iterator();
        List<Addon> addons = new ArrayList<>();

        int count = 0;
        while (addonIterator.hasNext()) {
            Addon addon = addonIterator.next();
            if (count++ >= 36 || !addonIterator.hasNext()) {
                addons.add(addon);
                //addons.add(addon);
                populate(new ArrayList<>(addons));
                addons.clear();
                count = 0;
            } else
                addons.add(addon);
        }
    }

    private ItemStack getAddonItemStack(Addon addon) {
        AddonMetadata addonMetadata = addon.getAddonMetadata();
        Material icon = addon.getIcon();


        return ItemFactory.newFactory(icon).setDisplayName("&b" + addonMetadata.getName())
                .addLoreLines(
                        addon.isEnabled() ? "&a&lEnabled" : "&c&lDisabled",
                        "&8&l&m          ",
                        "&r&bVersion: &fv" + addonMetadata.getVersion(),
                        "&r&bAuthor(s): &f" + String.join(",", addonMetadata.getAuthors())
                ).build();
    }

    private int pages = 0;

    public void populate(List<Addon> addons) {
        Frame frame = new Frame();
        for (int i = 0; i < addons.size(); i++) {
            if (i >= 36) break;
            Addon addon = addons.get(i);
            int[] coords = InventoryUtils.calculateXYFromSlot(i);
            int x = coords[0];
            int y = coords[1];


            //System.out.println("Adding to GUI " + addon.getAddonMetadata().getName() + " to slot " + i + " (X: " + x + " | Y: " + y + ")");
            SimpleButton simpleButton = new SimpleButton(
                    getAddonItemStack(addon),
                    click -> {
                        if (click.getInventoryClickEvent().getWhoClicked() instanceof Player) {
                            Player p = (Player) click.getInventoryClickEvent().getWhoClicked();
                            p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
                            addon.openSettingsMenu(p);
                        }
                    }
            );
            entries.add(new AbstractMap.SimpleEntry<>(addon, simpleButton));
            frame.registerComponent(simpleButton, x, y);

        }


        //Filling black panes before control row
        for (int i = 1; i <= 9; i++) {
            frame.registerComponent(new SimpleButton(
                    ItemFactory.newFactory(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").hideAttributes().build(),
                    onClick -> {
                    }
            ), i, 5);
        }

        Button prev = super.getPreviousButton(
                ItemFactory.newFactory(Material.ARROW).setDisplayName("&9&l< Previous").hideAttributes().build(),
                Sound.BLOCK_WOODEN_BUTTON_CLICK_ON
        );
        Button next = super.getNextButton(
                ItemFactory.newFactory(Material.ARROW).setDisplayName("&9&lNext >").hideAttributes().build(),
                Sound.BLOCK_WOODEN_BUTTON_CLICK_ON
        );

        if (super.getCurrentPage() - 1 < 0) prev.setRenderAllowed(false);
        if (super.getPages().size() == 0) next.setRenderAllowed(false);

        frame.registerComponent(prev, 1, 6);
        frame.registerComponent(new SimpleButton(
                ItemFactory.newFactory(Material.BOOK).setDisplayName("&9&lClose").hideAttributes().build(),
                onClick -> {
                    if (onClick.getInventoryClickEvent().getWhoClicked() instanceof Player) {
                        Player p = (Player) onClick.getInventoryClickEvent().getWhoClicked();
                        p.playSound(p.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1f, 1f);
                    }
                    onClick.getInventoryClickEvent().getWhoClicked().closeInventory();
                }
        ), 5, 6);
        frame.registerComponent(next, 9, 6);

        //System.out.println(pages);
        super.addFrames(pages++, frame);
    }
}
