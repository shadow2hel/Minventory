package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.constants.VALUABLES;
import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.data.managers.IPlayerInventoryManager;
import me.shadow2hel.minventory.model.EntityItemTracker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Item;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public class PlayerInteractListener implements Listener {
    private final IPlayerInventoryManager playerManager;
    private final IEntityManager entityManager;
    private final JavaPlugin main;

    public PlayerInteractListener(IPlayerInventoryManager playerManager, IEntityManager entityManager, JavaPlugin main) {
        this.playerManager = playerManager;
        this.entityManager = entityManager;
        this.main = main;
    }

    @EventHandler
    private void onEntityTouch(PlayerInteractEntityEvent touch) {
        Material itemInHand = touch.getPlayer().getInventory().getItemInMainHand().getType();
        if(touch.getRightClicked() instanceof Allay){
            if (!VALUABLES.GetAllBlacklist().contains(itemInHand)) {
                entityManager.createMobWithItem(new EntityItemTracker(touch.getRightClicked().getUniqueId().toString(),
                        touch.getRightClicked().getCustomName() != null,
                        touch.getRightClicked().getType().toString(),
                        (int)touch.getRightClicked().getLocation().getX(),
                        (int)touch.getRightClicked().getLocation().getY(),
                        (int)touch.getRightClicked().getLocation().getZ(),
                        touch.getRightClicked().getLocation().getWorld().getName()));
            } else {
                touch.setCancelled(true);
            }

        }
        if(touch.getRightClicked() instanceof ItemFrame) {
            if (!VALUABLES.GetAllBlacklist().contains(itemInHand)){
                //TODO LOG IN DATABASE
            } else {
                touch.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onArmorStandTouch(PlayerArmorStandManipulateEvent touch) {
        if (!VALUABLES.GetArmorWeaponsBlacklist().contains(touch.getPlayer().getInventory().getItemInMainHand().getType())){

            //TODO LOG IN DATABASE
        } else {
            touch.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent playerDropItemEvent) {
        Item droppedItem = playerDropItemEvent.getItemDrop();
        NamespacedKey key = new NamespacedKey(main, "minv_uuid");
        PersistentDataContainer nbt = droppedItem.getPersistentDataContainer();
        nbt.set(key, PersistentDataType.STRING, UUID.randomUUID().toString());
        if(nbt.has(key, PersistentDataType.STRING)) {
            String fetchedUuid = nbt.get(key, PersistentDataType.STRING);
            EntityItemTracker itemTracker = new EntityItemTracker(
                    fetchedUuid,
                    droppedItem.getCustomName() != null,
                    droppedItem.getType().toString(),
                    droppedItem.getLocation().getBlockX(),
                    droppedItem.getLocation().getBlockY(),
                    droppedItem.getLocation().getBlockZ(),
                    droppedItem.getLocation().getWorld().getName());
            entityManager.createMobWithItem(itemTracker);
        }
    }
}
