package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.constants.VALUABLES;
import me.shadow2hel.minventory.data.managers.IMobManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class MobPickupListener implements Listener {
    private final IMobManager mobManager;

    public MobPickupListener(IMobManager mobManager) {
        this.mobManager = mobManager;
    }

    @EventHandler
    private void onMobPickupItem(EntityPickupItemEvent pickupItemEvent) {
        if (pickupItemEvent.getEntityType() != EntityType.PLAYER && VALUABLES.GetAllBlacklist().contains(pickupItemEvent.getItem().getItemStack().getType())) {
            Bukkit.broadcastMessage(String.format("%s %s just picked up %s",
                    pickupItemEvent.getEntityType(),
                    pickupItemEvent.getEntity().getUniqueId(),
                    pickupItemEvent.getItem().getItemStack().getType()));
            //TODO LOG IN DATABASE
        }
    }
}