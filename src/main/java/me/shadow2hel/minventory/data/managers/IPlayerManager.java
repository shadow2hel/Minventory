package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.model.TouchedInventory;

public interface IPlayerManager {
    TouchedInventory createTouchedInventory(TouchedInventory touchedInventory);
    TouchedInventory updateTouchedInventory(TouchedInventory touchedInventory);
    TouchedInventory readTouchedInventory(String UUID);
    TouchedInventory readTouchedInventory(TouchedInventory touchedInventory);
    boolean deleteTouchedInventory(TouchedInventory touchedInventory);
}
