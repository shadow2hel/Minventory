package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.repositories.IEntityRepo;
import me.shadow2hel.minventory.model.EntityItemTracker;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class EntityItemsManager implements IEntityManager {
    IEntityRepo mobWithItemRepo;
    JavaPlugin main;

    public EntityItemsManager(IEntityRepo mobWithItemRepo, JavaPlugin main) {
        this.mobWithItemRepo = mobWithItemRepo;
        this.main = main;
    }

    public EntityItemTracker createMobWithItem(EntityItemTracker entityItemTracker) {
        EntityItemTracker newEntityItemTracker = mobWithItemRepo.createMobWithItem(entityItemTracker);
        if (newEntityItemTracker != null) {
            main.getLogger().info(String.format("Registered %s %s at %s %s %s",
                    newEntityItemTracker.getType(),
                    newEntityItemTracker.getUUID(),
                    newEntityItemTracker.getLocation_x(),
                    newEntityItemTracker.getLocation_y(),
                    newEntityItemTracker.getLocation_z()));
            return newEntityItemTracker;
        }
        main.getLogger().info(String.format("Register %s %s at %s %s %s failed!",
                entityItemTracker.getType(),
                entityItemTracker.getUUID(),
                entityItemTracker.getLocation_x(),
                entityItemTracker.getLocation_y(),
                entityItemTracker.getLocation_z()));
        return mobWithItemRepo.createMobWithItem(entityItemTracker);
    }

    public EntityItemTracker updateMobWithItem(EntityItemTracker entityItemTracker) {
        EntityItemTracker updatedEntityItemTracker = mobWithItemRepo.updateMobWithItem(entityItemTracker);
        return mobWithItemRepo.updateMobWithItem(entityItemTracker);
    }

    public List<EntityItemTracker> readAllMobWithItem() {
        return mobWithItemRepo.readAllMobWithItem();
    }

    public EntityItemTracker readMobWithItem(String UUID) {
        return mobWithItemRepo.readMobWithItem(UUID);
    }

    public EntityItemTracker readMobWithItem(EntityItemTracker entityItemTracker) {
        return mobWithItemRepo.readMobWithItem(entityItemTracker);
    }

    public boolean deleteMobWithItem(EntityItemTracker entityItemTracker) {
        boolean deleted = mobWithItemRepo.deleteMobWithItem(entityItemTracker);
        if (deleted) {
            main.getLogger().info(String.format("Deleted %s %s", entityItemTracker.getType(), entityItemTracker.getUUID()));
        } else {
            main.getLogger().info(String.format("Delete %s %s failed!", entityItemTracker.getType(), entityItemTracker.getUUID()));
        }
        return deleted;
    }
}
