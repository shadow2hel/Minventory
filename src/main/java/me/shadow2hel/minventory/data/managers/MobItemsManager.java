package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.Database;
import me.shadow2hel.minventory.data.repositories.IMobWithItemRepo;
import me.shadow2hel.minventory.data.repositories.MobWithItemRepo;
import me.shadow2hel.minventory.model.MobWithItem;
import org.bukkit.plugin.java.JavaPlugin;

public class MobItemsManager implements IMobManager {
    IMobWithItemRepo mobWithItemRepo;

    public MobItemsManager(Database db, JavaPlugin main) {
        this.mobWithItemRepo = new MobWithItemRepo(db, main);
    }

    public MobWithItem createMobWithItem(MobWithItem mobWithItem) {
        return mobWithItemRepo.createMobWithItem(mobWithItem);
    }

    public MobWithItem updateMobWithItem(MobWithItem mobWithItem) {
        return mobWithItemRepo.updateMobWithItem(mobWithItem);
    }

    public MobWithItem readMobWithItem(String UUID) {
        return mobWithItemRepo.readMobWithItem(UUID);
    }

    public MobWithItem readMobWithItem(MobWithItem mobWithItem) {
        return mobWithItemRepo.readMobWithItem(mobWithItem);
    }

    public boolean deleteMobWithItem(MobWithItem mobWithItem) {
        return mobWithItemRepo.deleteMobWithItem(mobWithItem);
    }
}
