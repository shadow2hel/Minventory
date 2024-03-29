package me.shadow2hel.minventory.commands;

import me.shadow2hel.minventory.Wiper;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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
