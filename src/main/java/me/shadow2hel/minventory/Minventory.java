package me.shadow2hel.minventory;

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
        getServer().getPluginManager().registerEvents(new InventoryOpenListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
        getServer().getPluginManager().registerEvents(new MobPickupListener(), this);
    }
}
