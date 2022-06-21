package me.shadow2hel.minventory.data.managers;

import me.shadow2hel.minventory.data.repositories.IWipeRepo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Date;
import java.util.List;

public class WipeManager implements IWipeManager {
    IWipeRepo wipeRepo;
    JavaPlugin main;

    public WipeManager(IWipeRepo wipeRepo, JavaPlugin main) {
        this.wipeRepo = wipeRepo;
        this.main = main;
    }

    @Override
    public Date createWipe(Date date) {
        return wipeRepo.createWipe(date);
    }

    @Override
    public List<Date> readAllWipes() {
        return wipeRepo.readAllWipes();
    }

    @Override
    public Date readLatestWipe() {
        return wipeRepo.readLatestWipe();
    }
}
