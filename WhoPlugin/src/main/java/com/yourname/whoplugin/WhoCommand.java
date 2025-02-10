package com.yourname.whoplugin;

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

import java.io.File;

public class WhoCommand  implements CommandExecutor {
    private final WhoPlugin plugin;
    private final DataManager configManager;
    public WhoCommand(WhoPlugin plugin, DataManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

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
        sender.sendMessage(ChatColor.GREEN + target.getName() + " has this information: ");
        String info;
        String targetuu = target.getUniqueId().toString();
        String path = targetuu + ".school";
        info = configManager.getConfig().getString(path);
        sender.sendMessage( "School: " + info);
        path = targetuu + ".grade";
        info = configManager.getConfig().getString(path);
        sender.sendMessage( "Grade: " + info);
        path = targetuu + ".name";
        info = configManager.getConfig().getString(path);
        sender.sendMessage( "Name: " + info);
        path = targetuu + ".pronouns";
        info = configManager.getConfig().getString(path);
        sender.sendMessage( "Pronouns: " + info);
        path = targetuu + ".role";
        info = configManager.getConfig().getString(path);
        sender.sendMessage( "Role: " + info);
        path = targetuu + ".ip";
        info = configManager.getConfig().getString(path);
        FileConfiguration config = loadOtherPluginYAML("TempPunishPlugin", "mutes.yml");
        path = "mutedPlayers." + targetuu;
        String value = config.getString(path);
        if (target.isBanned())
        {
            sender.sendMessage(ChatColor.RED + "Status: Banned");
        }
        else if (Bukkit.getBanList(BanList.Type.IP).isBanned(info)) {
            sender.sendMessage(ChatColor.RED + "Status: Banned");
        }
        else if (value != null)
        {
            sender.sendMessage(ChatColor.YELLOW + "Status: Muted");
        }
        else
        {
            sender.sendMessage(ChatColor.GREEN + "Status: Clear");
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective("money");
        if (objective == null) {
            sender.sendMessage(ChatColor.RED + "No such objective found.");
            return true;
        }
        Score score = objective.getScore(target.getName());
        float scoreValue = score.getScore();
        sender.sendMessage(ChatColor.GREEN + "Balance: " + scoreValue / 100 + " Pix");
        sender.sendMessage(ChatColor.GOLD + "-------------------------");
        return true;

    }
    public FileConfiguration loadOtherPluginYAML(String pluginName, String fileName) {
        File file = new File("plugins/" + pluginName + "/" + fileName);
        if (!file.exists()) {
            return null; // File doesn't exist
        }
        return YamlConfiguration.loadConfiguration(file);
    }
}
