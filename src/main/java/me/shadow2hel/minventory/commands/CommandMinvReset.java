package me.shadow2hel.minventory.commands;

import me.shadow2hel.minventory.Wiper;
import me.shadow2hel.minventory.constants.KEYS;
import me.shadow2hel.minventory.constants.VALUABLES;
import me.shadow2hel.minventory.pdc.PDCUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class CommandMinvReset implements CommandExecutor {
    Wiper wiper;
    JavaPlugin main;

    public CommandMinvReset(JavaPlugin main, Wiper wiper) {
        this.main = main;
        this.wiper = wiper;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        main.getLogger().info("Running wipe..");
        wiper.wipe();
        Bukkit.getLogger().info("Wipe done !");
        return true;
    }

}
