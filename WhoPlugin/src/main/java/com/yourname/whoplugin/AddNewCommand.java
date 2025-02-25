package com.yourname.whoplugin;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class AddNewCommand  implements CommandExecutor {
    private final WhoPlugin plugin;
    private final DataManager configManager;

    public AddNewCommand(WhoPlugin plugin, DataManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player player = (Player) sender;
        } catch (ClassCastException e) {

        }

        if(args.length == 6)
        {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            String targetuu = target.getUniqueId().toString();
            LuckPermsManager lpManager = new LuckPermsManager();

            if (args[5].equals("Developer"))
            {
                target.setOp(false);
                lpManager.addOfflinePlayerToGroup(target.getUniqueId(), "developer");
                lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "loyal");
                lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "ambassador");

            }
            else if (args[5].equals("Admin"))
            {
                if (sender instanceof ConsoleCommandSender) {
                    target.setOp(true);
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "ambassador");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "loyal");
                    lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "developer");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "You are not authorized to make users this level");
                    return true;
                }

            }
            else if (args[5].equals("Loyal"))
            {
                lpManager.addOfflinePlayerToGroup(target.getUniqueId(), "loyal");
                target.setOp(false);
                lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "ambassador");
                lpManager.removeOfflinePlayerFromGroup(target.getUniqueId(), "developer");
            }
            else if (args[5].equals("Ambassador"))
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
            String path = targetuu + ".school";
            if (configManager.getConfig().get(path) == null) {

                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                Objective objective = scoreboard.getObjective("money");
                if (objective == null) {
                    sender.sendMessage(ChatColor.RED + "No such objective found.");
                    return true;
                }
                Score score = objective.getScore(target.getName());
                score.setScore(800);
            }

            path = targetuu + ".school";
            configManager.getConfig().set(path, args[1]);
            path = targetuu + ".grade";
            configManager.getConfig().set(path, args[2]);
            path = targetuu + ".name";
            configManager.getConfig().set(path, args[3]);
            path = targetuu + ".pronouns";
            configManager.getConfig().set(path, args[4]);
            path = targetuu + ".role";
            configManager.getConfig().set(path, args[5]);
            if (!Bukkit.getServer().getWhitelistedPlayers().contains(target)) {
                Bukkit.getServer().getWhitelistedPlayers().add(target);
                target.setWhitelisted(true); // Mark them as whitelisted
                Bukkit.getServer().reloadData(); // Optional: reload the whitelist data
            }



            configManager.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Player added!");
            return true;

        }
        return false;
    }
}

