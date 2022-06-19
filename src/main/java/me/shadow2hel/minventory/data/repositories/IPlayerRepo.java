package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.model.ModelPlayer;

import java.util.List;

public interface IPlayerRepo {
    ModelPlayer createPlayer(ModelPlayer player);
    List<ModelPlayer> readAllPlayers();
    ModelPlayer readPlayer(String UUID);
    ModelPlayer readPlayer(ModelPlayer player);
    ModelPlayer updatePlayer(ModelPlayer player);
    boolean deletePlayer(ModelPlayer player);
}
