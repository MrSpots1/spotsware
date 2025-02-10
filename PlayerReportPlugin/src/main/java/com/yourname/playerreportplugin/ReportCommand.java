package com.yourname.playerreportplugin;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReportCommand implements CommandExecutor {

    private final ReportManager reportManager;

    public ReportCommand(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /report <player> <reason>");
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can report others.");
            return true;
        }

        // Cast sender to Player to access player-specific methods
        Player reporter = (Player) sender;

        String reportedPlayerName = args[0];
        // Join the remaining arguments to form the reason for the report
        String reason = String.join(" ", Arrays.copyOfRange(args, 1, args.length));




        // Create a new report and add it to the manager
        Report report = new Report(reporter.getName(), reportedPlayerName, reason);
        reportManager.addReport(report);

        // Notify the reporter that the report has been filed
        sender.sendMessage(ChatColor.GREEN + "Your report has been filed against " + reportedPlayerName + " for: " + reason);

        // Broadcast the report to all admins (players with the permission "playerreportplugin.viewreports")
        for (Player player : reporter.getServer().getOnlinePlayers()) {
            if (player.hasPermission("playerreportplugin.viewreports")) {
                player.sendMessage(ChatColor.RED + "A report has been filed against " + reportedPlayerName + " for: " + reason);
            }
        }

        return true;
    }
}
