package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.repositories.IPlayerRepo;
import me.shadow2hel.minventory.model.PlayerTracker;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlayerManager implements IPlayerManager {
    IPlayerRepo playerRepo;
    JavaPlugin main;

    public PlayerManager(IPlayerRepo playerRepo, JavaPlugin main) {
        this.playerRepo = playerRepo;
        this.main = main;
    }

    @Override
    public PlayerTracker createPlayer(PlayerTracker player) {

        PlayerTracker newPlayer = playerRepo.createPlayer(player);
        if (newPlayer != null) {
            main.getLogger().info(String.format("Registered Player %s", newPlayer.getUUID()));
            return newPlayer;
        }
        main.getLogger().info("Failed to register Player " + player.getUUID());
        return null;
    }

    @Override
    public List<PlayerTracker> readAllPlayers() {
        return playerRepo.readAllPlayers();
    }

    @Override
    public PlayerTracker readPlayer(String UUID) {
        return playerRepo.readPlayer(UUID);
    }

    @Override
    public PlayerTracker readPlayer(PlayerTracker player) {
        return playerRepo.readPlayer(player);
    }

    @Override
    public PlayerTracker updatePlayer(PlayerTracker player) {

        PlayerTracker updatedPlayer = playerRepo.updatePlayer(player);
        if (updatedPlayer != null) {
            main.getLogger().info("Updated Player " + updatedPlayer.getUUID());
            return updatedPlayer;
        }
        main.getLogger().info("Update Player " + player.getUUID() + " failed!");
        return null;
    }

    @Override
    public boolean deletePlayer(PlayerTracker player) {
        boolean deleted = playerRepo.deletePlayer(player);
        if (deleted) {
            main.getLogger().info("Deleted Player " + player.getUUID());
        } else {
            main.getLogger().info("Delete Player " + player.getUUID() + " failed!");
        }
        return deleted;
    }
}
