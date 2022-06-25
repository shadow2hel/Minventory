package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.constants.KEYS;
import me.shadow2hel.minventory.constants.VALUABLES;
import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.model.EntityItemTracker;
import me.shadow2hel.minventory.pdc.PDCUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Calendar;
import java.util.Date;

public class MobPickupListener implements Listener {
    private final JavaPlugin main;
    private final IEntityManager mobManager;
    private final IPlayerManager playerManager;


    public MobPickupListener(JavaPlugin main, IEntityManager mobManager, IPlayerManager playerManager) {
        this.main = main;
        this.mobManager = mobManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    private void onMobPickupItem(EntityPickupItemEvent pickupItemEvent) {
        if (pickupItemEvent.getEntityType() != EntityType.PLAYER && VALUABLES.GetAllBlacklist().contains(pickupItemEvent.getItem().getItemStack().getType())) {
            if (pickupItemEvent.getEntity() instanceof InventoryHolder inventoryHolder) {
                if (PDCUtils.getNbt(main, pickupItemEvent.getEntity(), KEYS.LASTWIPED, PersistentDataType.LONG) == null)
                    PDCUtils.setNbt(main, pickupItemEvent.getEntity(), KEYS.LASTWIPED, PersistentDataType.LONG, Calendar.getInstance().getTimeInMillis());
            }

        }
    }
}
