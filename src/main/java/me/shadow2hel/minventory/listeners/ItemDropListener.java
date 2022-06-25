package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.constants.KEYS;
import me.shadow2hel.minventory.pdc.PDCUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class ItemDropListener implements Listener {
    JavaPlugin main;

    public ItemDropListener(JavaPlugin main) {
        this.main = main;
    }

    @EventHandler
    private void onBlockDrop(BlockDropItemEvent dropItemEvent) {
        List<Entity> items = new ArrayList<>();
        if (dropItemEvent.getBlockState() instanceof InventoryHolder inventoryHolder) {
            Location blockLoc = inventoryHolder.getInventory().getLocation();
            items = blockLoc.getWorld().getNearbyEntities(blockLoc, 3, 3, 3, entity -> entity instanceof Item).stream().toList();
        } else {
            items = dropItemEvent.getItems()
                    .stream()
                    .map(item -> (Entity) item)
                    .collect(Collectors.toList());
        }
        items.forEach(item -> {
            Calendar cal = Calendar.getInstance();
            if (PDCUtils.getNbt(main, item, KEYS.LASTWIPED, PersistentDataType.LONG) == null) {
                PDCUtils.setNbt(main, item, KEYS.LASTWIPED, PersistentDataType.LONG, cal.getTimeInMillis());
            }
        });
    }

    @EventHandler
    private void onVehicleDestroy(VehicleDestroyEvent vehicleDestroyEvent) {

    }
}
