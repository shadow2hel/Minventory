package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.repositories.IPlayerInventoryRepo;
import me.shadow2hel.minventory.model.InventoryTracker;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlayerInventoryManager implements IPlayerInventoryManager {
    IPlayerInventoryRepo playerInventoryRepo;
    JavaPlugin main;

    public PlayerInventoryManager(IPlayerInventoryRepo playerInventoryRepo, JavaPlugin main) {
        this.playerInventoryRepo = playerInventoryRepo;
        this.main = main;
    }

    public InventoryTracker createTouchedInventory(InventoryTracker inventoryTracker) {
        InventoryTracker newInventoryTracker = playerInventoryRepo.createPlayerInventory(inventoryTracker);
        if(newInventoryTracker != null) {
            main.getLogger().info(String.format("Registered %s at %s %s %s",
                    newInventoryTracker.getType(),
                    newInventoryTracker.getLocationX(),
                    newInventoryTracker.getLocationY(),
                    newInventoryTracker.getLocationZ()));
            return newInventoryTracker;
        }
        main.getLogger().info(String.format("Register %s at %s %s %s failed!",
                inventoryTracker.getType(),
                inventoryTracker.getLocationX(),
                inventoryTracker.getLocationY(),
                inventoryTracker.getLocationZ()));
        return null;
    }

    public InventoryTracker updateTouchedInventory(InventoryTracker inventoryTracker) {
        InventoryTracker updatedInventory = playerInventoryRepo.updatePlayerInventory(inventoryTracker);
        if (updatedInventory != null) {
            main.getLogger().info(String.format("Updated %s at %s %s %s",
                    updatedInventory.getType(),
                    updatedInventory.getLocationX(),
                    updatedInventory.getLocationY(),
                    updatedInventory.getLocationZ()));
            return updatedInventory;
        }
        main.getLogger().info(String.format("Update %s at %s %s %s failed!",
                inventoryTracker.getType(),
                inventoryTracker.getLocationX(),
                inventoryTracker.getLocationY(),
                inventoryTracker.getLocationZ()));
        return null;
    }

    public List<InventoryTracker> readAllTouchedInventory() {
        return playerInventoryRepo.readAllPlayerInventory();
    }

    public InventoryTracker readTouchedInventory(String UUID) {
        return playerInventoryRepo.readPlayerInventory(UUID);
    }

    public InventoryTracker readTouchedInventory(InventoryTracker inventoryTracker) {
        return playerInventoryRepo.readPlayerInventory(inventoryTracker);
    }

    public boolean deleteTouchedInventory(InventoryTracker inventoryTracker) {
        boolean deleted = playerInventoryRepo.deletePlayerInventory(inventoryTracker);
        if(deleted) {
            main.getLogger().info(String.format("Deleted %s at %s %s %s",
                    inventoryTracker.getType(),
                    inventoryTracker.getLocationX(),
                    inventoryTracker.getLocationY(),
                    inventoryTracker.getLocationZ()));
        } else {
            main.getLogger().info(String.format("Delete %s at %s %s %s failed!",
                    inventoryTracker.getType(),
                    inventoryTracker.getLocationX(),
                    inventoryTracker.getLocationY(),
                    inventoryTracker.getLocationZ()));
        }
        return deleted;
    }
}
