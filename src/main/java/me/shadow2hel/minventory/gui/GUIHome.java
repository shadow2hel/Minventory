package me.shadow2hel.minventory.gui;

import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.model.PlayerTracker;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class GUIHome extends GUIScreen {
    private IPlayerManager playerManager;

    public GUIHome(JavaPlugin main, Player player, IPlayerManager playerManager) {
        super(main, player, 27, "Home");
        this.playerManager = playerManager;
    }

    @Override
    protected void initializeGrid() {
        int middle = 3;
        int right = 6;
        int rows = getInventory().getSize() / 9;
        for (int x = 0; x < getInventory().getSize(); x++) {
            int y = x / 9;
            int relativex = x - 9 * y;
            if (y == rows / 2) {
                if ( relativex == 4) {
                    ItemStack itemPrestige = createGuiItem(Material.SHULKER_BOX, "Prestige",
                            "Want to unlock Shulker Boxes?", "Click here!");
                    addCommandtoItem(itemPrestige, "minvgui prestige");
                    getInventory().setItem(x, itemPrestige);

                }
            }
        }
    }

}
