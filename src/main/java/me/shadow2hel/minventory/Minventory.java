package me.shadow2hel.minventory;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.SQLite;
import me.shadow2hel.minventory.data.managers.IMobManager;
import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.data.managers.MobItemsManager;
import me.shadow2hel.minventory.data.managers.PlayerInventoryManager;
import me.shadow2hel.minventory.listeners.InventoryOpenListener;
import me.shadow2hel.minventory.listeners.MobPickupListener;
import me.shadow2hel.minventory.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Minventory extends JavaPlugin {
    @Override
    public void onDisable() {
        Bukkit.getLogger().info(ChatColor.RED + "Disabled " + this.getName());
    }

    @Override
    public void onEnable() {
        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        Database sqlLite = new SQLite(this);
        IPlayerManager playerManager = new PlayerInventoryManager(sqlLite, this);
        IMobManager mobManager = new MobItemsManager(sqlLite, this);
        getServer().getPluginManager().registerEvents(new InventoryOpenListener(playerManager, mobManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(playerManager), this);
        getServer().getPluginManager().registerEvents(new MobPickupListener(mobManager), this);
    }

}
