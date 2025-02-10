package com.yourname.temppunishplugin;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    public UnmuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /unmute <player>");
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

        muteManager.unmute(target);
        sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been unmuted.");
        if (onlineTarget != null) {
            onlineTarget.sendMessage(ChatColor.GREEN + "You have been unmuted.");
        }
        return true;
    }
}
