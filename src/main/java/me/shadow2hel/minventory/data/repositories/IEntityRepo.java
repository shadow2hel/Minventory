package me.shadow2hel.minventory.data.repositories;

import me.shadow2hel.minventory.model.EntityItemTracker;

import java.util.List;

public interface IEntityRepo {
    EntityItemTracker createMobWithItem(EntityItemTracker entityItemTracker);
    List<EntityItemTracker> readAllMobWithItem();
    EntityItemTracker readMobWithItem(String UUID);
    EntityItemTracker readMobWithItem(EntityItemTracker entityItemTracker);
    EntityItemTracker updateMobWithItem(EntityItemTracker entityItemTracker);
    boolean deleteMobWithItem(EntityItemTracker entityItemTracker);
}
