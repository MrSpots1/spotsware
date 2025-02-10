package com.yourname.temppunishplugin;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;

public class TempMuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    public TempMuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /tempmute <player> <time>");
            return false;
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

        long durationInSeconds = parseDuration(args[1]);
        if (durationInSeconds <= 0) {
            sender.sendMessage(ChatColor.RED + "Invalid duration format.");
            return false;
        }

        muteManager.tempMute(target, durationInSeconds);
        sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been muted for " + args[1] + ".");
        if (onlineTarget != null) {
            onlineTarget.sendMessage(ChatColor.RED + "You have been muted for " + args[1] + ".");
        }
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
