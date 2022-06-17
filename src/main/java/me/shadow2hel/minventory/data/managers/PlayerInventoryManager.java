package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.repositories.IPlayerInventoryRepo;
import me.shadow2hel.minventory.data.repositories.PlayerInventoryRepo;
import me.shadow2hel.minventory.model.TouchedInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerInventoryManager implements IPlayerManager {
    IPlayerInventoryRepo playerInventoryRepo;

    public PlayerInventoryManager(Database db, JavaPlugin main) {
        this.playerInventoryRepo = new PlayerInventoryRepo(db, main);
    }

    public TouchedInventory createTouchedInventory(TouchedInventory touchedInventory) {
        return playerInventoryRepo.createPlayerInventory(touchedInventory);
    }

    public TouchedInventory updateTouchedInventory(TouchedInventory touchedInventory) {
        return playerInventoryRepo.updatePlayerInventory(touchedInventory);
    }

    public TouchedInventory readTouchedInventory(String UUID) {
        return playerInventoryRepo.readPlayerInventory(UUID);
    }

    public TouchedInventory readTouchedInventory(TouchedInventory touchedInventory) {
        return playerInventoryRepo.readPlayerInventory(touchedInventory);
    }

    public boolean deleteTouchedInventory(TouchedInventory touchedInventory) {
        return playerInventoryRepo.deletePlayerInventory(touchedInventory);
    }
}
