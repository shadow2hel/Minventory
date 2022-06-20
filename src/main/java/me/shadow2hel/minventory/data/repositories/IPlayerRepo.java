package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.model.PlayerTracker;

import java.util.List;

public interface IPlayerRepo {
    PlayerTracker createPlayer(PlayerTracker player);
    List<PlayerTracker> readAllPlayers();
    PlayerTracker readPlayer(String UUID);
    PlayerTracker readPlayer(PlayerTracker player);
    PlayerTracker updatePlayer(PlayerTracker player);
    boolean deletePlayer(PlayerTracker player);
}
