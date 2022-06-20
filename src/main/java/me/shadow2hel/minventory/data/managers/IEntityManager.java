package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.model.EntityItemTracker;

import java.util.List;

public interface IEntityManager {
    EntityItemTracker createMobWithItem(EntityItemTracker entityItemTracker);
    EntityItemTracker updateMobWithItem(EntityItemTracker entityItemTracker);
    List<EntityItemTracker> readAllMobWithItem();
    EntityItemTracker readMobWithItem(String UUID);
    EntityItemTracker readMobWithItem(EntityItemTracker entityItemTracker);
    boolean deleteMobWithItem(EntityItemTracker entityItemTracker);
}
