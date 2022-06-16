package me.shadow2hel.minventory.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

import java.util.ArrayList;

public class InventoryOpenListener implements Listener {
    private final ArrayList<InventoryType> blackList;

    public InventoryOpenListener() {
        blackList = new ArrayList<>();
        blackList.add(InventoryType.GRINDSTONE);
        blackList.add(InventoryType.ANVIL);
        blackList.add(InventoryType.BEACON);
        blackList.add(InventoryType.BREWING);
        blackList.add(InventoryType.CARTOGRAPHY);
        blackList.add(InventoryType.ENCHANTING);
        blackList.add(InventoryType.LECTERN);
        blackList.add(InventoryType.LOOM);
        blackList.add(InventoryType.SMITHING);
        blackList.add(InventoryType.MERCHANT);
        blackList.add(InventoryType.STONECUTTER);
        blackList.add(InventoryType.WORKBENCH);
    }

    @EventHandler
    public void onInventoryTouch(InventoryOpenEvent touch) {
        if (!blackList.contains(touch.getInventory().getType())) {
            if(touch.getInventory().getHolder() instanceof AbstractHorse horseling) {
                Bukkit.broadcastMessage(String.format("%s touched %s %s", touch.getPlayer().getName(), horseling.getName(), horseling.getUniqueId()));
            } else {
                Bukkit.broadcastMessage(String.format("%s opened %s at %s %s",
                        touch.getPlayer().getName(),
                        touch.getInventory().getType(),
                        (int)Math.floor(touch.getInventory().getLocation().getX()),
                        (int)Math.floor(touch.getInventory().getLocation().getZ())));
            }

        }


    }
}
