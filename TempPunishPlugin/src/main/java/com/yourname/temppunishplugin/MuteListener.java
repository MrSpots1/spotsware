package com.yourname.temppunishplugin;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.entity.Player;

public class MuteListener implements Listener {

    private final MuteManager muteManager;

    public MuteListener(MuteManager muteManager) {
        this.muteManager = muteManager;
    }

    // Block chat messages for muted players
    @EventHandler
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        if (muteManager.isMuted(player)) {
            event.setCancelled(true);  // Block chat
            player.sendMessage(ChatColor.RED + "You are muted and cannot send messages.");
        }
    }

    // Block commands for muted players
    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (muteManager.isMuted(player)) {
            String command = event.getMessage();
            if(command.startsWith("/me") || command.startsWith("/minecraft:me") || command.startsWith("/msg") || command.startsWith("/minecraft:msg") || command.startsWith("/say") || command.startsWith("/minecraft:say") || command.startsWith("/tell") || command.startsWith("/minecraft:tell") || command.startsWith("/tm") || command.startsWith("/minecraft:tm") || command.startsWith("/teammsg") || command.startsWith("/minecraft:teammsg") || command.startsWith("/w ") || command.startsWith("/minecraft:w ")) {
                event.setCancelled(true);  // Block command execution
                player.sendMessage(ChatColor.RED + "You are muted and cannot execute this command.");
            }
        }
    }
}
