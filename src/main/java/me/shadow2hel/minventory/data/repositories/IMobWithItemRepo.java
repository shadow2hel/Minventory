package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.model.MobWithItem;

public interface IMobWithItemRepo {
    MobWithItem createMobWithItem(MobWithItem mobWithItem);
    MobWithItem readMobWithItem(String UUID);
    MobWithItem readMobWithItem(MobWithItem mobWithItem);
    MobWithItem updateMobWithItem(MobWithItem mobWithItem);
    boolean deleteMobWithItem(MobWithItem mobWithItem);
}
