package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.repositories.IPlayerRepo;
import me.shadow2hel.minventory.model.ModelPlayer;

import java.util.List;

public class PlayerManager implements IPlayerManager {
    IPlayerRepo playerRepo;

    public PlayerManager(IPlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @Override
    public ModelPlayer createPlayer(ModelPlayer player) {
        return playerRepo.createPlayer(player);
    }

    @Override
    public List<ModelPlayer> readAllPlayers() {
        return playerRepo.readAllPlayers();
    }

    @Override
    public ModelPlayer readPlayer(String UUID) {
        return playerRepo.readPlayer(UUID);
    }

    @Override
    public ModelPlayer readPlayer(ModelPlayer player) {
        return playerRepo.readPlayer(player);
    }

    @Override
    public ModelPlayer updatePlayer(ModelPlayer player) {
        return playerRepo.updatePlayer(player);
    }

    @Override
    public boolean deletePlayer(ModelPlayer player) {
        return playerRepo.deletePlayer(player);
    }
}
