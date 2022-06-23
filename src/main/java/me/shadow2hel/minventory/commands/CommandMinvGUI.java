package me.shadow2hel.minventory.commands;

import me.shadow2hel.minventory.gui.GUIConfirmation;
import me.shadow2hel.minventory.gui.GUIScreen;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandMinvGUI implements CommandExecutor {
    JavaPlugin main;

    public CommandMinvGUI(JavaPlugin main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GUIScreen newGUI = new GUIConfirmation(main, (Player)sender);
        return true;
    }
}
