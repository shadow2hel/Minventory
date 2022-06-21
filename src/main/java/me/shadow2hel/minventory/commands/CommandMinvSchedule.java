package me.shadow2hel.minventory.commands;

import me.shadow2hel.minventory.Wiper;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandMinvSchedule implements CommandExecutor {
    Wiper wiper;
    JavaPlugin main;

    public CommandMinvSchedule(JavaPlugin main, Wiper wiper) {
        this.wiper = wiper;
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        wiper.scheduleWipe();
        return true;
    }
}
