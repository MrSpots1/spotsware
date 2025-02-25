package com.yourname.damagecontrolplugin;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.ChatColor;

import java.io.File;

public class ToggleDamageCommand implements CommandExecutor {
    private final DamageControlPlugin plugin;
    private final DataManager configManager;
    public ToggleDamageCommand(DamageControlPlugin plugin, DataManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 2)
        {
            return false;
        }
        if (!(args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("false")))
        {
            sender.sendMessage(ChatColor.RED + "Please enter true or false");
            return true;
        }




        if (args[0].equals("mace"))
        {
            configManager.getConfig().set("damage.mace", args[1]);
            configManager.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Updated!");
            return true;
        }
        else if (args[0].equals("cart"))
        {
            configManager.getConfig().set("damage.cart", args[1]);
            configManager.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Updated!");
            return true;
        }
        else if (args[0].equals("crystal"))
        {
            configManager.getConfig().set("damage.crystal", args[1]);
            configManager.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Updated!");
            return true;
        }
        else {
            sender.sendMessage(ChatColor.RED + "Invalid damage type");
        }




        return false;
    }

    public FileConfiguration loadOtherPluginYAML(String pluginName, String fileName) {
        File file = new File("plugins/" + pluginName + "/" + fileName);
        if (!file.exists()) {
            return null; // File doesn't exist
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
