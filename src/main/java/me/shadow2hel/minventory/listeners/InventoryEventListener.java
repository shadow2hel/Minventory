package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.constants.KEYS;
import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.data.managers.IPlayerInventoryManager;
import me.shadow2hel.minventory.gui.GUIScreen;
import me.shadow2hel.minventory.model.EntityItemTracker;
import me.shadow2hel.minventory.model.InventoryTracker;
import me.shadow2hel.minventory.pdc.PDCUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Calendar;

public class InventoryEventListener implements Listener {
    private final JavaPlugin main;
    private final ArrayList<InventoryType> blackList;
    private final IPlayerInventoryManager playerManager;
    private final IEntityManager mobManager;

    public InventoryEventListener(JavaPlugin main, IPlayerInventoryManager playerManager, IEntityManager mobManager) {
        this.main = main;
        this.playerManager = playerManager;
        this.mobManager = mobManager;
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
        if (!(touch.getInventory().getHolder() instanceof GUIScreen) && !blackList.contains(touch.getInventory().getType())) {
            Calendar cal = Calendar.getInstance();
            if (touch.getInventory().getHolder() instanceof  DoubleChest doubleChest ) {
                InventoryHolder leftSide = doubleChest.getLeftSide();
                InventoryHolder rightSide = doubleChest.getRightSide();
                if (PDCUtils.getNbt(main, (PersistentDataHolder) leftSide, KEYS.LASTWIPED, PersistentDataType.LONG) == null)
                    PDCUtils.setNbt(main, (PersistentDataHolder) leftSide, KEYS.LASTWIPED, PersistentDataType.LONG, cal.getTimeInMillis());
                if (PDCUtils.getNbt(main, (PersistentDataHolder) rightSide, KEYS.LASTWIPED, PersistentDataType.LONG) == null)
                    PDCUtils.setNbt(main, (PersistentDataHolder) rightSide, KEYS.LASTWIPED, PersistentDataType.LONG, cal.getTimeInMillis());
            } else {
                if (PDCUtils.getNbt(main, (PersistentDataHolder) touch.getInventory().getHolder(), KEYS.LASTWIPED, PersistentDataType.LONG) == null)
                    PDCUtils.setNbt(main, (PersistentDataHolder) touch.getInventory().getHolder(), KEYS.LASTWIPED, PersistentDataType.LONG, cal.getTimeInMillis());
            }
        }
    }

    @EventHandler
    private void onHopperMove(InventoryMoveItemEvent inventoryMoveItemEvent) {
        InventoryHolder source = inventoryMoveItemEvent.getSource().getHolder();
        InventoryHolder target = inventoryMoveItemEvent.getDestination().getHolder();
        InventoryHolder hopper = inventoryMoveItemEvent.getInitiator().getHolder();
        Calendar cal = Calendar.getInstance();
        if (PDCUtils.getNbt(main, (PersistentDataHolder) source, KEYS.LASTWIPED, PersistentDataType.LONG) == null)
            PDCUtils.setNbt(main, (PersistentDataHolder) source, KEYS.LASTWIPED, PersistentDataType.LONG, cal.getTimeInMillis());
        if (PDCUtils.getNbt(main, (PersistentDataHolder) target, KEYS.LASTWIPED, PersistentDataType.LONG) == null)
            PDCUtils.setNbt(main, (PersistentDataHolder) target, KEYS.LASTWIPED, PersistentDataType.LONG, cal.getTimeInMillis());
        if (PDCUtils.getNbt(main, (PersistentDataHolder) hopper, KEYS.LASTWIPED, PersistentDataType.LONG) == null)
            PDCUtils.setNbt(main, (PersistentDataHolder) hopper, KEYS.LASTWIPED, PersistentDataType.LONG, cal.getTimeInMillis());
    }

    @EventHandler
    private void onHopperPickup(InventoryPickupItemEvent hopperPickupEvent) {
        InventoryHolder hopper = hopperPickupEvent.getInventory().getHolder();
        Calendar cal = Calendar.getInstance();
        if (PDCUtils.getNbt(main, (PersistentDataHolder) hopper, KEYS.LASTWIPED, PersistentDataType.LONG) == null)
            PDCUtils.setNbt(main, (PersistentDataHolder) hopper, KEYS.LASTWIPED, PersistentDataType.LONG, cal.getTimeInMillis());
    }

}
