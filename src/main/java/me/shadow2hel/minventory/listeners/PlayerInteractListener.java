package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.constants.VALUABLES;
import me.shadow2hel.minventory.data.managers.IMobManager;
import me.shadow2hel.minventory.data.managers.IPlayerInventoryManager;
import me.shadow2hel.minventory.model.MobWithItem;
import me.shadow2hel.minventory.model.TouchedInventory;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Allay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractListener implements Listener {
    private final IPlayerInventoryManager playerManager;
    private final IMobManager mobManager;

    public PlayerInteractListener(IPlayerInventoryManager playerManager, IMobManager mobManager) {
        this.playerManager = playerManager;
        this.mobManager = mobManager;
    }

    @EventHandler
    private void onEntityTouch(PlayerInteractEntityEvent touch) {
        Material itemInHand = touch.getPlayer().getInventory().getItemInMainHand().getType();
        if(touch.getRightClicked() instanceof Allay){
            if (!VALUABLES.GetAllBlacklist().contains(itemInHand)) {
                mobManager.createMobWithItem(new MobWithItem(touch.getRightClicked().getUniqueId().toString(),
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
    private void onDoublechestBreak(BlockBreakEvent blockBreakEvent) {
        if (blockBreakEvent.getBlock().getState() instanceof DoubleChest doubleChest) {
            for (int x = -1; x < 1; x++) {
                for (int z = -1; z < 1; z++) {
                    if (x != 0 && z != 0) {
                       Block possibleBlock = doubleChest.getWorld().getBlockAt(doubleChest.getLocation().getBlockX() + x, doubleChest.getLocation().getBlockY(), doubleChest.getLocation().getBlockZ() + z);
                       if (possibleBlock instanceof Chest leftoverDoublechest) {
                           playerManager.createTouchedInventory(new TouchedInventory(
                                   blockBreakEvent.getPlayer().getUniqueId().toString(),
                                   leftoverDoublechest.getInventory().getType().toString(),
                                   leftoverDoublechest.getLocation().getBlockX(),
                                   leftoverDoublechest.getLocation().getBlockY(),
                                   leftoverDoublechest.getLocation().getBlockZ(),
                                   leftoverDoublechest.getWorld().getName()));
                       }
                    }
                }
            }
        }
    }
}
