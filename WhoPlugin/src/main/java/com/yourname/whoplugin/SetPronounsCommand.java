package com.yourname.whoplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
public class SetPronounsCommand  implements CommandExecutor {
    private final WhoPlugin plugin;
    private final DataManager configManager;
    public SetPronounsCommand(WhoPlugin plugin, DataManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0)
        {
            Player senderPlayer = sender.getServer().getPlayer(sender.getName());
            String senderuu = senderPlayer.getUniqueId().toString();
            String path = senderuu + ".pronouns";
            String data = "";
            for (int i = 0; i < args.length; i++) {
                data = data + args[i] + " ";
            }
            configManager.getConfig().set(path, data);
            configManager.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Pronouns set!");
            return true;
        }
        return false;
    }
}

