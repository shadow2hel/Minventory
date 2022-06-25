package me.shadow2hel.minventory.commands;

import me.shadow2hel.minventory.constants.KEYS;
import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.gui.GUIConfirmation;
import me.shadow2hel.minventory.gui.GUIHome;
import me.shadow2hel.minventory.gui.GUIInfo;
import me.shadow2hel.minventory.gui.GUIScreen;
import me.shadow2hel.minventory.model.PlayerTracker;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandMinvGUI implements CommandExecutor {
    JavaPlugin main;
    IPlayerManager playerManager;

    public CommandMinvGUI(JavaPlugin main, IPlayerManager playerManager) {
        this.main = main;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length == 0) {
                GUIScreen home = new GUIHome(main, (Player) sender, playerManager);
                home.openInventory();
            } else {
                if (args.length == 1 && args[0].equalsIgnoreCase("prestige")) {
                    createPrestigeGUI(player);
                }

                if (args.length == 2 && args[0].equalsIgnoreCase("prestige") && args[1].equalsIgnoreCase("confirm")) {
                    PlayerTracker dataPlayer = playerManager.readPlayer(player.getUniqueId().toString());
                    if (!dataPlayer.isMarkedForPrestige() && hasItemsForPrestige(player)) {
                        ItemStack item = createGuiItem(Material.SHULKER_BOX, "Are you sure you want to prestige?");
                        GUIScreen currentScreen = player.getOpenInventory().getTopInventory().getHolder() instanceof GUIScreen ? (GUIScreen) player.getOpenInventory().getTopInventory().getHolder() : null;
                        GUIScreen guiPrestigeConfirm = new GUIConfirmation(main, player, currentScreen, item, "minvgui prestige confirm accept");
                        guiPrestigeConfirm.openInventory();
                    }
                }

                if (args.length == 3 && args[0].equalsIgnoreCase("prestige") && args[1].equalsIgnoreCase("confirm") && args[2].equalsIgnoreCase("accept")) {
                    markForPrestige(player);
                    createPrestigeGUI(player);
                }
            }


        } else {
            main.getLogger().info("Only a player may use this command!");
            return false;
        }
        return true;
    }

    private ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    private void createPrestigeGUI(Player player) {
        HashMap<Integer, ItemStack> items = new HashMap<>();
        PlayerTracker dataPlayer = playerManager.readPlayer(player.getUniqueId().toString());
        ItemStack itemStack;
        if (dataPlayer.isMarkedForPrestige()) {
            itemStack = createGuiItem(Material.RED_SHULKER_BOX, "Prestige " + dataPlayer.getPrestige(),
                    "You will prestige at the next wipe!",
                    "You have unlocked " + dataPlayer.getPrestige() + " shulker boxes so far");
        } else if (!dataPlayer.isMarkedForPrestige() && dataPlayer.getPrestige() >= KEYS.PRESTIGEMAX - 1) {
            itemStack = createGuiItem(Material.RED_SHULKER_BOX, "Prestige " + dataPlayer.getPrestige(),
                    ChatColor.BOLD + "You have unlocked all the prestiges!",
                    "You have unlocked " + dataPlayer.getPrestige() + " shulker boxes so far");
        } else {
            itemStack = createGuiItem(Material.GREEN_SHULKER_BOX, "Prestige " + dataPlayer.getPrestige(),
                    "Click here to prestige!",
                    "You have unlocked " + dataPlayer.getPrestige() + " shulker boxes so far");
            addCommandtoItem(itemStack, "minvgui prestige confirm");
            ItemStack requiredDragonEgg = createGuiItem(Material.DRAGON_EGG, "REQUIRED TO PROGRESS");
            ItemStack requiredBeacons = createGuiItem(Material.BEACON, "REQUIRED TO PROGRESS");
            requiredBeacons.setAmount(dataPlayer.getPrestige() + 1);
            items.put(21, requiredDragonEgg);
            items.put(22, requiredBeacons);
        }
        if (dataPlayer.getPrestige() > 0)
            itemStack.setAmount(dataPlayer.getPrestige());
        items.put(13, itemStack);
        GUIScreen prestigeGUI = new GUIInfo(main, player, 27, "Prestige", items);
        prestigeGUI.openInventory();
    }

    private boolean hasItemsForPrestige(Player player) {
        PlayerTracker playerTracker = playerManager.readPlayer(player.getUniqueId().toString());
        ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG, 1);
        ItemStack beacons = new ItemStack(Material.BEACON, playerTracker.getPrestige() + 1);
        return player.getInventory().containsAtLeast(dragonEgg, 1)
                && player.getInventory().containsAtLeast(beacons, playerTracker.getPrestige() + 1);
    }

    private void markForPrestige(Player player) {
        PlayerTracker playerTracker = playerManager.readPlayer(player.getUniqueId().toString());
        if (!playerTracker.isMarkedForPrestige()) {
            if (hasItemsForPrestige(player)) {
                ItemStack dragonEgg = new ItemStack(Material.DRAGON_EGG, 1);
                ItemStack beacons = new ItemStack(Material.BEACON, playerTracker.getPrestige() + 1);
                player.getInventory().removeItem(dragonEgg);
                player.getInventory().removeItem(beacons);
                playerTracker.setMarkedForPrestige(true);
                playerManager.updatePlayer(playerTracker);
            }

        }

    }

    private void addCommandtoItem(ItemStack item, String command) {
        NamespacedKey key = new NamespacedKey(main, "minv-item-command");
        ItemMeta nbt = item.getItemMeta();
        if (nbt == null) return;
        nbt.getPersistentDataContainer().set(key, PersistentDataType.STRING, command);
        item.setItemMeta(nbt);
    }
}
