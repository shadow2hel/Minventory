package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.model.TouchedInventory;

public interface IPlayerInventoryRepo {
    TouchedInventory createPlayerInventory(TouchedInventory touchedInventory);
    TouchedInventory readPlayerInventory(String UUID);
    TouchedInventory readPlayerInventory(TouchedInventory touchedInventory);
    TouchedInventory updatePlayerInventory(TouchedInventory touchedInventory);
    boolean deletePlayerInventory(TouchedInventory touchedInventory);
}
