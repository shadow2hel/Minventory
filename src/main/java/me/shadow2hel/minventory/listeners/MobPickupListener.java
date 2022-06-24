package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.constants.VALUABLES;
import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.model.EntityItemTracker;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class MobPickupListener implements Listener {
    private final IEntityManager mobManager;
    private final IPlayerManager playerManager;

    public MobPickupListener(IEntityManager mobManager, IPlayerManager playerManager) {
        this.mobManager = mobManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    private void onMobPickupItem(EntityPickupItemEvent pickupItemEvent) {
        if (pickupItemEvent.getEntityType() != EntityType.PLAYER && VALUABLES.GetAllBlacklist().contains(pickupItemEvent.getItem().getItemStack().getType())) {
            mobManager.createMobWithItem(new EntityItemTracker(
                    pickupItemEvent.getEntity().getUniqueId().toString(),
                    pickupItemEvent.getEntity().getCustomName() != null,
                    pickupItemEvent.getEntityType().toString(),
                    (int)pickupItemEvent.getEntity().getLocation().getX(),
                    (int)pickupItemEvent.getEntity().getLocation().getY(),
                    (int)pickupItemEvent.getEntity().getLocation().getZ(),
                    pickupItemEvent.getEntity().getLocation().getWorld().getName()));
        }
    }
}
