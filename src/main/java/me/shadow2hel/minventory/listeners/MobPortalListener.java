package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.Wiper;
import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.model.EntityItemTracker;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class MobPortalListener implements Listener {
    private JavaPlugin main;
    private IEntityManager mobManager;
    private Wiper wiper;


    public MobPortalListener(JavaPlugin main, IEntityManager mobManager, Wiper wiper) {
        this.main = main;
        this.mobManager = mobManager;
        this.wiper = wiper;
    }

//    @EventHandler
//    public void onMobTeleport(EntityPortalExitEvent exitEvent) {
//        Bukkit.getLogger().info("exit portal");
//        Entity entity = exitEvent.getEntity();
//        if (!(entity instanceof Player)) {
//            EntityItemTracker dataMob = mobManager.readMobWithItem(entity.getUniqueId().toString());
//            if (dataMob != null && !dataMob.getWorld().equals(entity.getWorld().toString()) ) {
//                EntityItemTracker updateMob = new EntityItemTracker(
//                        entity.getUniqueId().toString(),
//                        entity.getCustomName() != null,
//                        entity.getType().toString(),
//                        (int)entity.getLocation().getX(),
//                        (int)entity.getLocation().getY(),
//                        (int)entity.getLocation().getZ(),
//                        entity.getWorld().getUID().toString()
//                );
//                mobManager.updateMobWithItem(updateMob);
//            }
//        }
//    }

    @EventHandler
    public void onMobPortal(EntityPortalEvent portalEvent) {
        wiper.stopTrackingEntities();
        Entity entity = portalEvent.getEntity();




        Bukkit.getScheduler().runTaskLater(main, task -> {
            String uuid = entity.getUniqueId().toString();
            Bukkit.getLogger().info("entity: " + entity.getLocation().toString());
            Bukkit.getLogger().info("newLoc: " + portalEvent.getTo().toString());
            if (entity instanceof Item item) {
                NamespacedKey key = new NamespacedKey(main, "minv_uuid");
                PersistentDataContainer nbt = item.getPersistentDataContainer();
                if (nbt.has(key, PersistentDataType.STRING)) {
                    uuid = nbt.get(key, PersistentDataType.STRING);
                }
            }
            EntityItemTracker dataMob = mobManager.readMobWithItem(uuid);
            if (dataMob != null && !dataMob.getWorld().equals(entity.getWorld().getUID().toString()) ) {
                EntityItemTracker updateMob = new EntityItemTracker(
                        uuid,
                        entity.getCustomName() != null,
                        entity.getType().toString(),
                        (int)portalEvent.getTo().getX(),
                        (int)portalEvent.getTo().getY(),
                        (int)portalEvent.getTo().getZ(),
                        portalEvent.getTo().getWorld().getUID().toString()
                );
                mobManager.updateMobWithItem(updateMob);
            }
            wiper.startTrackingEntites();
        }, 1L);

    }
}
