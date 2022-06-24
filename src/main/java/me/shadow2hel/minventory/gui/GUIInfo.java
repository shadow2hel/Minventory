package me.shadow2hel.minventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class GUIInfo extends GUIScreen {

    public GUIInfo(JavaPlugin main, Player player, int inventorySize, String inventoryName, Map<Integer, ItemStack> itemsToAdd) {
        super(main, player, inventorySize, inventoryName, itemsToAdd);
    }

    @Override
    protected void initializeGrid() {
        super.items.forEach((index, itemStack) -> getInventory().setItem(index, itemStack));
    }
}
