package me.shadow2hel.minventory;

import me.shadow2hel.minventory.commands.CommandMinvReset;
import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.SQLite;
import me.shadow2hel.minventory.data.managers.*;
import me.shadow2hel.minventory.data.repositories.MobWithItemRepo;
import me.shadow2hel.minventory.data.repositories.PlayerInventoryRepo;
import me.shadow2hel.minventory.data.repositories.PlayerRepo;
import me.shadow2hel.minventory.listeners.*;
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
        IPlayerInventoryManager playerInventoryManager = new PlayerInventoryManager(new PlayerInventoryRepo(sqlLite, this), this);
        IMobManager mobManager = new MobItemsManager(new MobWithItemRepo(sqlLite, this), this);
        IPlayerManager playerManager = new PlayerManager(new PlayerRepo(sqlLite, this),this);
        Wiper wiper = new Wiper(mobManager, playerInventoryManager, playerManager, this);
        initializeListeners(playerInventoryManager, mobManager, playerManager, wiper);
        this.getCommand("wipe").setExecutor(new CommandMinvReset(this, wiper));
    }

    private void initializeListeners(IPlayerInventoryManager playerInventoryManager,
                                     IMobManager mobManager, IPlayerManager playerManager, Wiper wiper) {
        getServer().getPluginManager().registerEvents(new InventoryEventListener(playerInventoryManager, mobManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(playerInventoryManager, mobManager), this);
        getServer().getPluginManager().registerEvents(new MobPickupListener(mobManager), this);
        getServer().getPluginManager().registerEvents(new MobPortalListener(mobManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(playerManager, wiper), this);
    }

}
