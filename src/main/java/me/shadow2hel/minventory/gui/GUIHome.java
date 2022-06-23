package me.shadow2hel.minventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GUIHome extends GUIScreen {

    public GUIHome(JavaPlugin main, Player player, int inventorySize, String inventoryName) {
        super(main, player, inventorySize, inventoryName);
    }

    @Override
    protected void initializeGrid() {

    }

    @Override
    protected void onClick(InventoryClickEvent clickEvent) {

    }
}
