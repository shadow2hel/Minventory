package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.model.MobWithItem;

import java.util.List;

public interface IMobWithItemRepo {
    MobWithItem createMobWithItem(MobWithItem mobWithItem);
    List<MobWithItem> readAllMobWithItem();
    MobWithItem readMobWithItem(String UUID);
    MobWithItem readMobWithItem(MobWithItem mobWithItem);
    MobWithItem updateMobWithItem(MobWithItem mobWithItem);
    boolean deleteMobWithItem(MobWithItem mobWithItem);
}
