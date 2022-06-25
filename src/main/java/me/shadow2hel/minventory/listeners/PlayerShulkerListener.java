package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.model.PlayerTracker;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerShulkerListener implements Listener {
    private final IPlayerManager playerManager;

    public PlayerShulkerListener(IPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    private void onPlayerPickup(EntityPickupItemEvent entityPickupItemEvent) {
        if (entityPickupItemEvent.getEntity() instanceof Player player) {
            int amountofShulkersOnPlayer = player.getInventory().all(Material.SHULKER_BOX).size();
            PlayerTracker dataPlayer = playerManager.readPlayer(player.getUniqueId().toString());
            if (amountofShulkersOnPlayer >= dataPlayer.getPrestige() && entityPickupItemEvent.getItem().getItemStack().getType() == Material.SHULKER_BOX) {
                entityPickupItemEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onPlayerMoveInventory(InventoryClickEvent cl) {
        if (cl.getWhoClicked() instanceof Player player) {
            PlayerTracker dataPlayer = playerManager.readPlayer(player.getUniqueId().toString());
            int amountofShulkersonPlayer = player.getInventory().all(Material.SHULKER_BOX).size();
            if (amountofShulkersonPlayer >= dataPlayer.getPrestige() && (cl.getCurrentItem() != null && cl.getCursor() != null)) {
                if (Objects.requireNonNull(cl.getCurrentItem()).getType() == Material.SHULKER_BOX && cl.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && !Objects.equals(cl.getClickedInventory(), player.getInventory())) {
                    cl.setCancelled(true);
                }
                if ((Objects.requireNonNull(cl.getCursor()).getType() == Material.SHULKER_BOX && Objects.equals(cl.getClickedInventory(), player.getInventory())) && (cl.getAction() == InventoryAction.PLACE_ALL
                || cl.getAction() == InventoryAction.PLACE_SOME
                        || cl.getAction() == InventoryAction.PLACE_ONE)) {
                    cl.setCancelled(true);
                }
            }

        }
    }

    @EventHandler
    private void onPlayerDragInventory(InventoryDragEvent drag) {
        if (drag.getWhoClicked() instanceof Player player) {
            PlayerTracker dataPlayer = playerManager.readPlayer(player.getUniqueId().toString());
            int amountofShulkersonPlayer = player.getInventory().all(Material.SHULKER_BOX).size();
            if (amountofShulkersonPlayer >= dataPlayer.getPrestige()) {
                int sizeContainer = drag.getView().getTopInventory().getSize() - 1;
                boolean withInPlayerInv = false;
                for (int slot : drag.getRawSlots()) {
                    if (!withInPlayerInv && slot > sizeContainer) {
                        withInPlayerInv = true;
                    }
                }
                if (withInPlayerInv)
                    drag.setCancelled(true);
            }
        }
    }
}
