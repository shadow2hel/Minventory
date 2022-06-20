package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.model.InventoryTracker;

import java.util.List;

public interface IPlayerInventoryManager {
    InventoryTracker createTouchedInventory(InventoryTracker inventoryTracker);

    InventoryTracker updateTouchedInventory(InventoryTracker inventoryTracker);
    List<InventoryTracker> readAllTouchedInventory();
    InventoryTracker readTouchedInventory(String UUID);
    InventoryTracker readTouchedInventory(InventoryTracker inventoryTracker);
    boolean deleteTouchedInventory(InventoryTracker inventoryTracker);
}
