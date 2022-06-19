package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.repositories.IMobWithItemRepo;
import me.shadow2hel.minventory.data.repositories.MobWithItemRepo;
import me.shadow2hel.minventory.model.MobWithItem;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MobItemsManager implements IMobManager {
    IMobWithItemRepo mobWithItemRepo;
    JavaPlugin main;

    public MobItemsManager(IMobWithItemRepo mobWithItemRepo, JavaPlugin main) {
        this.mobWithItemRepo = mobWithItemRepo;
        this.main = main;
    }

    public MobWithItem createMobWithItem(MobWithItem mobWithItem) {
        MobWithItem newMobWithItem = mobWithItemRepo.createMobWithItem(mobWithItem);
        if (newMobWithItem != null) {
            main.getLogger().info(String.format("Registered %s picked up item at %s %s %s",
                    newMobWithItem.getType(),
                    newMobWithItem.getLocation_x(),
                    newMobWithItem.getLocation_y(),
                    newMobWithItem.getLocation_z()));
            return newMobWithItem;
        }
        main.getLogger().info(String.format("Register %s picked up item at %s %s %s failed!",
                mobWithItem.getType(),
                mobWithItem.getLocation_x(),
                mobWithItem.getLocation_y(),
                mobWithItem.getLocation_z()));
        return mobWithItemRepo.createMobWithItem(mobWithItem);
    }

    public MobWithItem updateMobWithItem(MobWithItem mobWithItem) {
        MobWithItem updatedMobWithItem = mobWithItemRepo.updateMobWithItem(mobWithItem);
        return mobWithItemRepo.updateMobWithItem(mobWithItem);
    }

    public List<MobWithItem> readAllMobWithItem() {
        return mobWithItemRepo.readAllMobWithItem();
    }

    public MobWithItem readMobWithItem(String UUID) {
        return mobWithItemRepo.readMobWithItem(UUID);
    }

    public MobWithItem readMobWithItem(MobWithItem mobWithItem) {
        return mobWithItemRepo.readMobWithItem(mobWithItem);
    }

    public boolean deleteMobWithItem(MobWithItem mobWithItem) {
        boolean deleted = mobWithItemRepo.deleteMobWithItem(mobWithItem);
        if (deleted) {
            main.getLogger().info(String.format("Deleted %s %s", mobWithItem.getType(), mobWithItem.getUUID()));
        } else {
            main.getLogger().info(String.format("Delete %s %s failed!", mobWithItem.getType(), mobWithItem.getUUID()));
        }
        return deleted;
    }
}
