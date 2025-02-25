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


                if (args[2].equals( "Developer"))
                {

                    target.setOp(false);
                    lpManager.addOfflinePlayerToGroup(target.getUniqueId(), "developer");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "loyal");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "ambassador");
                }
                else if (args[2].equals("Admin") )
                {
                    if (sender instanceof ConsoleCommandSender) {
                        target.setOp(true);
                        lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "ambassador");
                        lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "loyal");
                        lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "developer");
                    }
                    else {
                        sender.sendMessage(ChatColor.RED + "You are not authorized to change users to this level");
                        return true;
                    }
                }
                else if (args[2].equals("Loyal"))
                {

                    lpManager.addOfflinePlayerToGroup(target.getUniqueId(), "loyal");
                    target.setOp(false);
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "ambassador");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "developer");
                }
                else if (args[2].equals("Ambassador"))
                {

                    lpManager.addOfflinePlayerToGroup(target.getUniqueId(), "ambassador");
                    target.setOp(false);
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "loyal");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "developer");
                }
                else
                {

                    target.setOp(false);
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "loyal");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "ambassador");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "developer");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid element.");
                return true;
            }
            String data = "";
            if (args.length > 3) {
                for (int i = 0; i < args.length - 2; i++) {
                    data = data + args[i + 2] + " ";
                }
            }
            else
            {
                data = args[2];
            }
            configManager.getConfig().set(path, data);
            configManager.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Player modified!");
            return true;


        }
        return false;
    }
}

