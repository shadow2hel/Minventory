package me.shadow2hel.minventory.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class GUIConfirmation extends GUIScreen {
    private final GUIScreen lastScreen;
    private final ItemStack infoItem;
    private final String acceptCommand;

    public GUIConfirmation(JavaPlugin main, Player player, GUIScreen lastScreen, ItemStack infoItem, String acceptCommand) {
        super(main, player, 27, ChatColor.DARK_GREEN + "Confirmation");
        this.lastScreen = lastScreen;
        this.infoItem = infoItem;
        this.acceptCommand = acceptCommand;
    }


    @Override
    protected void initializeGrid() {
        int middle = 3;
        int right = 6;
        int rows = getInventory().getSize() / 9;
        for (int x = 0; x < getInventory().getSize(); x++) {
            int y = x / 9;
            int relativex = x - 9 * y;
            ItemStack itemStack = null;
            if (relativex < middle) {
                itemStack = createGuiItem(Material.GREEN_STAINED_GLASS_PANE, "Accept");
            } else if (relativex >= right) {
                itemStack = createGuiItem(Material.RED_STAINED_GLASS_PANE, "Decline");
            } else {
                if (y == rows / 2 && relativex == 4)
                    itemStack = infoItem;
            }

            getInventory().setItem(x, itemStack);
        }

    }

    public void deny() {
        if (lastScreen != null) {
            getPlayer().closeInventory();
            getPlayer().openInventory(lastScreen.getInventory());
        } else {
            getPlayer().closeInventory();
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
    @Override
    @EventHandler
    public void onClick(final InventoryClickEvent clickEvent) {
        if (!clickEvent.getInventory().equals(getInventory())) return;

        clickEvent.setCancelled(true);

        final ItemStack clickedItem = clickEvent.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        if (clickedItem.getType() == Material.RED_STAINED_GLASS_PANE)
            deny();
        if (clickedItem.getType() == Material.GREEN_STAINED_GLASS_PANE) {
            Player player = (Player) clickEvent.getWhoClicked();
            player.performCommand(acceptCommand);
        }
    }
}
