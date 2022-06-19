package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.data.managers.IMobManager;
import me.shadow2hel.minventory.model.MobWithItem;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;

public class MobPortalListener implements Listener {
    IMobManager mobManager;

    public MobPortalListener(IMobManager mobManager) {
        this.mobManager = mobManager;
    }

    @EventHandler
    public void onMobTeleport(EntityPortalExitEvent exitEvent) {
        Entity entity = exitEvent.getEntity();
        MobWithItem dataMob = mobManager.readMobWithItem(entity.getUniqueId().toString());
        if (dataMob != null && !dataMob.getWorld().equals(entity.getWorld().toString()) ) {
            MobWithItem updateMob = new MobWithItem(
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
