package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.data.managers.IPlayerInventoryManager;
import me.shadow2hel.minventory.model.EntityItemTracker;
import me.shadow2hel.minventory.model.InventoryTracker;
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

import java.util.ArrayList;

public class InventoryEventListener implements Listener {
    private final ArrayList<InventoryType> blackList;
    private final IPlayerInventoryManager playerManager;
    private final IEntityManager mobManager;

    public InventoryEventListener(IPlayerInventoryManager playerManager, IEntityManager mobManager) {
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
        if (!blackList.contains(touch.getInventory().getType())) {
            if(touch.getInventory().getHolder() instanceof ChestedHorse ||
                    touch.getInventory().getHolder() instanceof ChestBoat ||
                    (touch.getInventory().getHolder() instanceof Minecart && touch.getInventory().getHolder() instanceof Vehicle)) {
                mobManager.createMobWithItem(new EntityItemTracker(
                        ((Vehicle) touch.getInventory().getHolder()).getUniqueId().toString(),
                        ((Vehicle) touch.getInventory().getHolder()).getCustomName() != null,
                        ((Vehicle) touch.getInventory().getHolder()).getType().toString(),
                        (int)((Vehicle) touch.getInventory().getHolder()).getLocation().getX(),
                        (int)((Vehicle) touch.getInventory().getHolder()).getLocation().getY(),
                        (int)((Vehicle) touch.getInventory().getHolder()).getLocation().getZ(),
                        ((Vehicle) touch.getInventory().getHolder()).getLocation().getWorld().getName()));
            } else {
                if (touch.getInventory().getType() != InventoryType.ENDER_CHEST) {
                    if (touch.getInventory().getHolder() instanceof DoubleChest doubleChest) {
                        DoubleChestInventory doubleChestInventory = (DoubleChestInventory) doubleChest.getInventory();
                        Block left = doubleChestInventory.getLeftSide().getLocation().getBlock();
                        Block right = doubleChestInventory.getRightSide().getLocation().getBlock();
                        InventoryTracker leftSide = new InventoryTracker(
                                touch.getPlayer().getUniqueId().toString(),
                                doubleChestInventory.getType().toString(),
                                left.getX(),
                                left.getY(),
                                left.getZ(),
                                left.getWorld().getName());
                        InventoryTracker rightSide = new InventoryTracker(
                                touch.getPlayer().getUniqueId().toString(),
                                doubleChestInventory.getType().toString(),
                                right.getX(),
                                right.getY(),
                                right.getZ(),
                                right.getWorld().getName());
                        playerManager.createTouchedInventory(leftSide);
                        playerManager.createTouchedInventory(rightSide);
                    } else {
                        playerManager.createTouchedInventory(new InventoryTracker(
                                touch.getPlayer().getUniqueId().toString(),
                                touch.getInventory().getType().toString(),
                                (int) touch.getInventory().getLocation().getX(),
                                (int) touch.getInventory().getLocation().getY(),
                                (int) touch.getInventory().getLocation().getZ(),
                                touch.getInventory().getLocation().getWorld().getName()));
                    }
                }
            }

        }


    }

    @EventHandler
    private void onHopperMove(InventoryMoveItemEvent inventoryMoveItemEvent) {
        InventoryHolder source = inventoryMoveItemEvent.getSource().getHolder();
        InventoryHolder target = inventoryMoveItemEvent.getDestination().getHolder();
        InventoryHolder hopper = inventoryMoveItemEvent.getInitiator().getHolder();
        if (source instanceof BlockInventoryHolder sourceChest) {
            InventoryTracker sourceContainer = new InventoryTracker(
                    "HOPPER_TRANSACTION_SOURCE",
                    sourceChest.getInventory().getType().toString(),
                    (int)sourceChest.getInventory().getLocation().getX(),
                    (int)sourceChest.getInventory().getLocation().getY(),
                    (int)sourceChest.getInventory().getLocation().getZ(),
                    sourceChest.getInventory().getLocation().getWorld().getName());
            playerManager.createTouchedInventory(sourceContainer);
        } else if (source instanceof Vehicle sourceEntity) {
            EntityItemTracker sourceContainer = new EntityItemTracker(
                    sourceEntity.getUniqueId().toString(),
                    sourceEntity.getCustomName() != null,
                    sourceEntity.getType().toString(),
                    (int)sourceEntity.getLocation().getX(),
                    (int)sourceEntity.getLocation().getY(),
                    (int)sourceEntity.getLocation().getZ(),
                    sourceEntity.getLocation().getWorld().getName()
            );
            mobManager.createMobWithItem(sourceContainer);
        }

        if (target instanceof BlockInventoryHolder targetChest) {
            InventoryTracker targetContainer = new InventoryTracker(
                    "HOPPER_TRANSACTION_TARGET",
                    targetChest.getInventory().getType().toString(),
                    (int)target.getInventory().getLocation().getX(),
                    (int)target.getInventory().getLocation().getY(),
                    (int)target.getInventory().getLocation().getZ(),
                    target.getInventory().getLocation().getWorld().getName());
            playerManager.createTouchedInventory(targetContainer);
        } else if (target instanceof Vehicle targetEntity) {
            EntityItemTracker targetContainer = new EntityItemTracker(
                    targetEntity.getUniqueId().toString(),
                    targetEntity.getCustomName() != null,
                    targetEntity.getType().toString(),
                    (int)targetEntity.getLocation().getX(),
                    (int)targetEntity.getLocation().getY(),
                    (int)targetEntity.getLocation().getZ(),
                    targetEntity.getLocation().getWorld().getName());
            mobManager.createMobWithItem(targetContainer);
        }

        if (hopper instanceof BlockInventoryHolder hopperChest) {
            InventoryTracker hopperContainer = new InventoryTracker(
                    "HOPPER_TRANSACTION_MOVER",
                    hopperChest.getInventory().getType().toString(),
                    (int)hopper.getInventory().getLocation().getX(),
                    (int)hopper.getInventory().getLocation().getY(),
                    (int)hopper.getInventory().getLocation().getZ(),
                    hopper.getInventory().getLocation().getWorld().getName());
            playerManager.createTouchedInventory(hopperContainer);
        } else if (hopper instanceof Vehicle hopperEntity) {
            EntityItemTracker hopperContainer = new EntityItemTracker(
                    hopperEntity.getUniqueId().toString(),
                    hopperEntity.getCustomName() != null,
                    hopperEntity.getType().toString(),
                    (int)hopperEntity.getLocation().getX(),
                    (int)hopperEntity.getLocation().getY(),
                    (int)hopperEntity.getLocation().getZ(),
                    hopperEntity.getLocation().getWorld().getName());
            mobManager.createMobWithItem(hopperContainer);
        }
    }

    @EventHandler
    private void onHopperPickup(InventoryPickupItemEvent hopperPickupEvent) {
        InventoryHolder hopper = hopperPickupEvent.getInventory().getHolder();
        Bukkit.getLogger().info("pikcup");
        if (hopper instanceof BlockInventoryHolder hopperChest) {
            InventoryTracker hopperContainer = new InventoryTracker(
                    "HOPPER_TRANSACTION_MOVER",
                    hopperChest.getInventory().getType().toString(),
                    (int)hopper.getInventory().getLocation().getX(),
                    (int)hopper.getInventory().getLocation().getY(),
                    (int)hopper.getInventory().getLocation().getZ(),
                    hopper.getInventory().getLocation().getWorld().getName());
            playerManager.createTouchedInventory(hopperContainer);
        } else if (hopper instanceof Vehicle hopperEntity) {
            EntityItemTracker hopperContainer = new EntityItemTracker(
                    hopperEntity.getUniqueId().toString(),
                    hopperEntity.getCustomName() != null,
                    hopperEntity.getType().toString(),
                    (int)hopperEntity.getLocation().getX(),
                    (int)hopperEntity.getLocation().getY(),
                    (int)hopperEntity.getLocation().getZ(),
                    hopperEntity.getLocation().getWorld().getName());
            mobManager.createMobWithItem(hopperContainer);
        }
    }

}
