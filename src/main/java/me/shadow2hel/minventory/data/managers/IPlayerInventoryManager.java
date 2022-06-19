package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.model.TouchedInventory;

import java.util.List;

public interface IPlayerInventoryManager {
    TouchedInventory createTouchedInventory(TouchedInventory touchedInventory);

    TouchedInventory updateTouchedInventory(TouchedInventory touchedInventory);
    List<TouchedInventory> readAllTouchedInventory();
    TouchedInventory readTouchedInventory(String UUID);
    TouchedInventory readTouchedInventory(TouchedInventory touchedInventory);
    boolean deleteTouchedInventory(TouchedInventory touchedInventory);
}
