package com.yourname.temppunishplugin;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MuteCommand implements CommandExecutor {

    private final MuteManager muteManager;

    public MuteCommand(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /mute <player>");
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

        muteManager.mute(target);
        sender.sendMessage(ChatColor.GREEN + "Player " + target.getName() + " has been permanently muted.");
        if (onlineTarget != null)
        {
            onlineTarget.sendMessage(ChatColor.RED + "You have been permanently muted.");
        }
        return true;
    }
}
