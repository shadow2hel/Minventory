package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.model.EntityItemTracker;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalExitEvent;

public class MobPortalListener implements Listener {
    IEntityManager mobManager;

    public MobPortalListener(IEntityManager mobManager) {
        this.mobManager = mobManager;
    }

    @EventHandler
    public void onMobTeleport(EntityPortalExitEvent exitEvent) {
        Entity entity = exitEvent.getEntity();
        if (!(entity instanceof Player)) {
            EntityItemTracker dataMob = mobManager.readMobWithItem(entity.getUniqueId().toString());
            if (dataMob != null && !dataMob.getWorld().equals(entity.getWorld().toString()) ) {
                EntityItemTracker updateMob = new EntityItemTracker(
                        entity.getUniqueId().toString(),
                        entity.getCustomName() != null,
                        entity.getType().toString(),
                        (int)entity.getLocation().getX(),
                        (int)entity.getLocation().getY(),
                        (int)entity.getLocation().getZ(),
                        entity.getWorld().toString()
                );
                mobManager.updateMobWithItem(updateMob);
            }
        }
    }
}
