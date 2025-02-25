package com.yourname.whoplugin;

import com.yourname.whoplugin.DataManager;
import com.yourname.whoplugin.WhoPlugin;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.awt.datatransfer.FlavorEvent;
import java.io.File;

public class PayCommand  implements CommandExecutor {
    private final WhoPlugin plugin;
    private final DataManager configManager;

    public PayCommand(WhoPlugin plugin, DataManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Only players can run this command!");
                return true;
            }
            OfflinePlayer target;
            Player onlineTarget = sender.getServer().getPlayer(args[0]);
            Player senderPlayer = (Player) sender;

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
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            Objective objective = scoreboard.getObjective("money");
            if (objective == null) {
                sender.sendMessage(ChatColor.RED + "No such objective found.");
                return true;
            }

            Score targetscore = objective.getScore(target.getName());
            Score senderscore = objective.getScore(senderPlayer.getName());
            Float paymentamount;
            try {
                paymentamount = Float.valueOf(args[1]);

            } catch (NumberFormatException e)
            {
                sender.sendMessage(ChatColor.RED + "The given payment amount is not a valid amount.");
                return true;
            }
            if (senderscore.getScore() * 100 < paymentamount * 100)
            {
                sender.sendMessage(ChatColor.RED + "Not enough money!");
                return true;
            }

            targetscore.setScore(targetscore.getScore() + Math.round(paymentamount * 100));
            senderscore.setScore(senderscore.getScore() - Math.round(paymentamount * 100));
            sender.sendMessage(ChatColor.GREEN + "Transaction Complete!");
            return true;
        }
        return false;
    }
}