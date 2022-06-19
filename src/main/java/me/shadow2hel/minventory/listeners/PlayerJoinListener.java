package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.Wiper;
import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.model.ModelPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final IPlayerManager playerManager;
    private final Wiper wiper;

    public PlayerJoinListener(IPlayerManager playerManager, Wiper wiper) {
        this.playerManager = playerManager;
        this.wiper = wiper;
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        ModelPlayer player = playerManager.readPlayer(playerJoinEvent.getPlayer().getUniqueId().toString());
        if (player != null) {
            if (!player.isEnderChestWiped()) {
                wiper.wipeEnderchest(playerJoinEvent.getPlayer());
            }
        } else {
            ModelPlayer freshPlayer = new ModelPlayer(playerJoinEvent.getPlayer().getUniqueId().toString(),
                    true, 0, false);
            playerManager.createPlayer(freshPlayer);
        }
    }
}
