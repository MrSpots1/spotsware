package com.yourname.playerreportplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ViewReportsCommand implements CommandExecutor {

    private final ReportManager reportManager;

    public ViewReportsCommand(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (reportManager.getReports().isEmpty()) {
            sender.sendMessage(ChatColor.RED + "There are no open reports.");
            return false;
        }

        sender.sendMessage(ChatColor.GREEN + "Open Reports:");

        for (Report report : reportManager.getReports()) {
            // Display who filed the report and who it was filed against
            sender.sendMessage(ChatColor.YELLOW + "Report ID: " + report.getReportID());
            sender.sendMessage(ChatColor.RED + "Filed by: " + report.getReporterName());
            sender.sendMessage(ChatColor.RED + "Against: " + report.getReportedPlayerName());
            sender.sendMessage(ChatColor.GREEN + "Reason: " + report.getReason());
            sender.sendMessage(ChatColor.GOLD + "------------------------------------");
        }

        return true;
    }
}
