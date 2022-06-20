package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.model.InventoryTracker;

import java.util.List;

public interface IPlayerInventoryRepo {
    InventoryTracker createPlayerInventory(InventoryTracker inventoryTracker);
    List<InventoryTracker> readAllPlayerInventory();
    InventoryTracker readPlayerInventory(String UUID);
    InventoryTracker readPlayerInventory(InventoryTracker inventoryTracker);
    InventoryTracker updatePlayerInventory(InventoryTracker inventoryTracker);
    boolean deletePlayerInventory(InventoryTracker inventoryTracker);
}
