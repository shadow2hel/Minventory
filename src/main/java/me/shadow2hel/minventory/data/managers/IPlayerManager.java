package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.model.ModelPlayer;

import java.util.List;

public interface IPlayerManager {
    ModelPlayer createPlayer(ModelPlayer player);
    List<ModelPlayer> readAllPlayers();
    ModelPlayer readPlayer(String UUID);
    ModelPlayer readPlayer(ModelPlayer player);
    ModelPlayer updatePlayer(ModelPlayer player);
    boolean deletePlayer(ModelPlayer player);
}
