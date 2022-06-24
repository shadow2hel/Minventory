package me.shadow2hel.minventory.gui;

import me.shadow2hel.minventory.model.EntityItemTracker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public abstract class GUIScreen implements Listener, InventoryHolder {
    private final JavaPlugin main;
    private final Inventory inv;
    private final Player player;
    protected final Map<Integer, ItemStack> items;

    public GUIScreen(JavaPlugin main, Player player, int inventorySize, String inventoryName) {
        this.main = main;
        this.player = player;
        this.inv = Bukkit.createInventory(this, inventorySize, inventoryName);
        this.items = null;
        attachListener();
        initializeGrid();
    }

    public GUIScreen(JavaPlugin main, Player player, int inventorySize, String inventoryName, Map<Integer, ItemStack> items) {
        this.main = main;
        this.player = player;
        this.inv = Bukkit.createInventory(this, inventorySize, inventoryName);
        this.items = items;
        attachListener();
        initializeGrid();
    }

    private void attachListener() {
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    protected abstract void initializeGrid();

    protected void addCommandtoItem(ItemStack item, String command) {
        NamespacedKey key = new NamespacedKey(main, "minv-item-command");
        ItemMeta nbt = item.getItemMeta();
        if (nbt == null) return;
        nbt.getPersistentDataContainer().set(key, PersistentDataType.STRING, command);
        item.setItemMeta(nbt);
    }

    @EventHandler
    public void onClick(InventoryClickEvent clickEvent) {
        if (!clickEvent.getInventory().equals(getInventory())) return;

        clickEvent.setCancelled(true);

        final ItemStack clickedItem = clickEvent.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        NamespacedKey key = new NamespacedKey(main, "minv-item-command");
        PersistentDataContainer nbt = clickedItem.getItemMeta().getPersistentDataContainer();
        if(nbt.has(key, PersistentDataType.STRING)) {
            String command = nbt.get(key, PersistentDataType.STRING);
            if (command != null && getMain().getServer().getPluginCommand(command.split(" ")[0]) != null) {
                Player player = (Player) clickEvent.getWhoClicked();
                player.performCommand(command);
            }
        }
    }

    @EventHandler
    public final void onInventoryDrag(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    public void openInventory() {
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

    protected Player getPlayer() {
        return player;
    }

    protected JavaPlugin getMain() {
        return main;
    }
}
