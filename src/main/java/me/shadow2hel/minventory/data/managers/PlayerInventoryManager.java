package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.repositories.IPlayerInventoryRepo;
import me.shadow2hel.minventory.data.repositories.PlayerInventoryRepo;
import me.shadow2hel.minventory.model.TouchedInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlayerInventoryManager implements IPlayerInventoryManager {
    IPlayerInventoryRepo playerInventoryRepo;
    JavaPlugin main;

    public PlayerInventoryManager(IPlayerInventoryRepo playerInventoryRepo, JavaPlugin main) {
        this.playerInventoryRepo = playerInventoryRepo;
        this.main = main;
    }

    public TouchedInventory createTouchedInventory(TouchedInventory touchedInventory) {
        TouchedInventory newTouchedInventory = playerInventoryRepo.createPlayerInventory(touchedInventory);
        if(newTouchedInventory != null) {
            main.getLogger().info(String.format("Registered %s at %s %s %s",
                    newTouchedInventory.getType(),
                    newTouchedInventory.getLocationX(),
                    newTouchedInventory.getLocationY(),
                    newTouchedInventory.getLocationZ()));
            return newTouchedInventory;
        }
        main.getLogger().info(String.format("Register %s at %s %s %s failed!",
                touchedInventory.getType(),
                touchedInventory.getLocationX(),
                touchedInventory.getLocationY(),
                touchedInventory.getLocationZ()));
        return null;
    }

    public TouchedInventory updateTouchedInventory(TouchedInventory touchedInventory) {
        TouchedInventory updatedInventory = playerInventoryRepo.updatePlayerInventory(touchedInventory);
        if (updatedInventory != null) {
            main.getLogger().info(String.format("Updated %s at %s %s %s",
                    updatedInventory.getType(),
                    updatedInventory.getLocationX(),
                    updatedInventory.getLocationY(),
                    updatedInventory.getLocationZ()));
            return updatedInventory;
        }
        main.getLogger().info(String.format("Update %s at %s %s %s failed!",
                touchedInventory.getType(),
                touchedInventory.getLocationX(),
                touchedInventory.getLocationY(),
                touchedInventory.getLocationZ()));
        return null;
    }

    public List<TouchedInventory> readAllTouchedInventory() {
        return playerInventoryRepo.readAllPlayerInventory();
    }

    public TouchedInventory readTouchedInventory(String UUID) {
        return playerInventoryRepo.readPlayerInventory(UUID);
    }

    public TouchedInventory readTouchedInventory(TouchedInventory touchedInventory) {
        return playerInventoryRepo.readPlayerInventory(touchedInventory);
    }

    public boolean deleteTouchedInventory(TouchedInventory touchedInventory) {
        boolean deleted = playerInventoryRepo.deletePlayerInventory(touchedInventory);
        if(deleted) {
            main.getLogger().info(String.format("Deleted %s at %s %s %s",
                    touchedInventory.getType(),
                    touchedInventory.getLocationX(),
                    touchedInventory.getLocationY(),
                    touchedInventory.getLocationZ()));
        } else {
            main.getLogger().info(String.format("Delete %s at %s %s %s failed!",
                    touchedInventory.getType(),
                    touchedInventory.getLocationX(),
                    touchedInventory.getLocationY(),
                    touchedInventory.getLocationZ()));
        }
        return deleted;
    }
}
