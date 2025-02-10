package com.yourname.whoplugin;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

public class PlayerModifyCommand  implements CommandExecutor {
    private final WhoPlugin plugin;
    private final DataManager configManager;

    public PlayerModifyCommand(WhoPlugin plugin, DataManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 2) {
            OfflinePlayer target;
            Player onlineTarget = sender.getServer().getPlayer(args[0]);


            if (onlineTarget != null) {
                target = onlineTarget; // Assign online player to target
            } else {
                // Try to get offline player
                target = sender.getServer().getOfflinePlayer(args[0]);

                if (target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
            }
            String targetuu = target.getUniqueId().toString();
            String path;
            if (args[1].equals("school")) {
                path = targetuu + ".school";
            } else if (args[1].equals("grade")) {
                path = targetuu + ".grade";
            } else if (args[1].equals("name")) {
                path = targetuu + ".name";
            } else if (args[1].equals("pronouns")) {
                path = targetuu + ".pronouns";
            } else if (args[1].equals("role")) {
                path = targetuu + ".role";
                LuckPermsManager lpManager = new LuckPermsManager();

                if (args[2] == "Developer")
                {
                    target.setOp(false);
                    lpManager.addOfflinePlayerToGroup(target.getUniqueId(), "Developer");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Loyal");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Ambassador");
                }
                else if (args[2] == "Admin")
                {
                    target.setOp(true);
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Ambassador");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Loyal");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Developer");
                }
                else if (args[2] == "Loyal")
                {
                    lpManager.addOfflinePlayerToGroup(target.getUniqueId(), "Loyal");
                    target.setOp(false);
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Ambassador");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Developer");
                }
                else if (args[2] == "Ambassador")
                {
                    lpManager.addOfflinePlayerToGroup(target.getUniqueId(), "Ambassador");
                    target.setOp(false);
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Loyal");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Developer");
                }
                else
                {
                    target.setOp(false);
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Loyal");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Ambassador");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "Developer");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid element.");
                return true;
            }
            String data = "";
            for (int i = 0; i < args.length - 2; i++) {
                data = data + args[i+2] + " ";
            }
            configManager.getConfig().set(path, data);
            configManager.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Player modified!");
            return true;


        }
        return false;
    }
}

