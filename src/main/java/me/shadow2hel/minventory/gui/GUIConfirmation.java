package me.shadow2hel.minventory.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class GUIConfirmation extends GUIScreen {

    public GUIConfirmation(JavaPlugin main, Player player) {
        super(main, player, 27, ChatColor.DARK_GREEN + "Confirmation");
        player.openInventory(getInventory());
    }


    @Override
    protected void initializeGrid() {
        int left = 0;
        int middle = 3;
        int right = 6;
        for (int x = 0; x < getInventory().getSize(); x++) {
            int y = x / 9;
            int relativex = x - 9 * y;
            ItemStack itemStack;
            if ((relativex < middle) || (relativex >= right)) {
                itemStack = createGuiItem(Material.BLACK_STAINED_GLASS_PANE, "SIDE", "HOE");
            } else {
                itemStack = createGuiItem(Material.DIAMOND, "MIDDLE", "CHILD");
            }

            getInventory().setItem(x, itemStack);
        }

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

    // Check for clicks on items
    @EventHandler
    protected void onClick(final InventoryClickEvent clickEvent) {
        if (!clickEvent.getInventory().equals(getInventory())) return;

        clickEvent.setCancelled(true);

        final ItemStack clickedItem = clickEvent.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) clickEvent.getWhoClicked();

        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + clickEvent.getRawSlot());
    }
}
