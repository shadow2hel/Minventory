package me.shadow2hel.minventory.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public abstract class GuiScreen implements Listener, InventoryHolder {
    private final JavaPlugin main;
    private final Inventory inv;
    private final Player player;

    public GuiScreen(JavaPlugin main, Player player, int inventorySize, String inventoryName) {
        this.main = main;
        this.player = player;
        this.inv = Bukkit.createInventory(this, inventorySize, inventoryName);
        attachListener();
    }

    private void attachListener() {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    protected abstract void onClick(final InventoryClickEvent clickEvent);

    @EventHandler
    private void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    protected void openInventory() {
        player.openInventory(inv);
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
