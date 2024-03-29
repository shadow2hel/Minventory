package me.shadow2hel.minventory;

import me.shadow2hel.minventory.commands.CommandMinvGUI;
import me.shadow2hel.minventory.commands.CommandMinvReset;
import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.SQLite;
import me.shadow2hel.minventory.data.managers.*;
import me.shadow2hel.minventory.data.repositories.EntityRepo;
import me.shadow2hel.minventory.data.repositories.PlayerInventoryRepo;
import me.shadow2hel.minventory.data.repositories.PlayerRepo;
import me.shadow2hel.minventory.data.repositories.WipeRepo;
import me.shadow2hel.minventory.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class Minventory extends JavaPlugin {
    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        Database sqlLite = new SQLite(this);
        IPlayerInventoryManager playerInventoryManager = new PlayerInventoryManager(new PlayerInventoryRepo(sqlLite, this), this);
        IEntityManager mobManager = new EntityItemsManager(new EntityRepo(sqlLite, this), this);
        IPlayerManager playerManager = new PlayerManager(new PlayerRepo(sqlLite, this),this);
        IWipeManager wipeManager = new WipeManager(new WipeRepo(sqlLite, this), this);
        Wiper wiper = new Wiper(mobManager, playerInventoryManager, playerManager, wipeManager,  this);
        initializeListeners(playerInventoryManager, mobManager, playerManager, wiper);
        initializeCommands(wiper, playerManager);
    }

    private void initializeListeners(IPlayerInventoryManager playerInventoryManager,
                                     IEntityManager mobManager, IPlayerManager playerManager, Wiper wiper) {
        getServer().getPluginManager().registerEvents(new InventoryEventListener(playerInventoryManager, mobManager), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(playerInventoryManager, mobManager, this), this);
        getServer().getPluginManager().registerEvents(new MobPickupListener(mobManager, playerManager), this);
        getServer().getPluginManager().registerEvents(new MobPortalListener(this, mobManager, wiper), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(playerManager, wiper), this);
        getServer().getPluginManager().registerEvents(new PlayerShulkerListener(playerManager), this);

    }

    private void initializeCommands(Wiper wiper, IPlayerManager playerManager) {
        this.getCommand("wipe").setExecutor(new CommandMinvReset(this, wiper));
        this.getCommand("minvgui").setExecutor(new CommandMinvGUI(this, playerManager));
    }
}
