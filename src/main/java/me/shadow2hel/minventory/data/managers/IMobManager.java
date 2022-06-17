package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.repositories.MobWithItemRepo;
import me.shadow2hel.minventory.model.MobWithItem;

public interface IMobManager {
    MobWithItem createMobWithItem(MobWithItem mobWithItem);
    MobWithItem updateMobWithItem(MobWithItem mobWithItem);
    MobWithItem readMobWithItem(String UUID);
    MobWithItem readMobWithItem(MobWithItem mobWithItem);
    boolean deleteMobWithItem(MobWithItem mobWithItem);
}
