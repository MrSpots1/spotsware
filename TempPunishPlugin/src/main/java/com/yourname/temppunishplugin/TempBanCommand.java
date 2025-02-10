package com.yourname.temppunishplugin;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TempBanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /tempban <player> <time> [reason]");
            return true;
        }

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

        long duration = parseDuration(args[1]);
        if (duration <= 0) {
            sender.sendMessage(ChatColor.RED + "Invalid duration format. Use 20m, 2h, 3d, 1M.");
            return true;
        }

        String reason = args.length > 2 ? String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)) : "Rule violation";
        Date expiry = Date.from(Instant.now().plus(duration, ChronoUnit.SECONDS));

        Bukkit.getBanList(BanList.Type.NAME).addBan(target.getName(), reason, expiry, sender.getName());
        if (onlineTarget != null) {
            onlineTarget.kickPlayer(ChatColor.RED + "You have been temporarily banned for: " + reason);
        }

        sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been banned for " + args[1] + ".");
        return true;
    }

    private long parseDuration(String input) {
        try {
            char unit = input.charAt(input.length() - 1);
            int value = Integer.parseInt(input.substring(0, input.length() - 1));

            return switch (unit) {
                case 'm' -> value * 60L;
                case 'h' -> value * 60L * 60;
                case 'd' -> value * 60L * 60 * 24;
                case 'M' -> value * 60L * 60 * 24 * 30;
                default -> -1;
            };
        } catch (Exception e) {
            return -1;
        }
    }
}
