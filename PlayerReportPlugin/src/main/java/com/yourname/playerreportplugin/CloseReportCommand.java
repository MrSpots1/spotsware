package com.yourname.playerreportplugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CloseReportCommand implements CommandExecutor {

    private final ReportManager reportManager;

    public CloseReportCommand(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /closereport <reportID>");
            return false;
        }

        String reportID = args[0];
        Report report = reportManager.getReportByID(reportID);

        if (report == null) {
            sender.sendMessage(ChatColor.RED + "Report not found.");
            return false;
        }

        // Remove the report from the list
        reportManager.removeReport(report);

        // Notify the sender
        sender.sendMessage(ChatColor.GREEN + "Report " + reportID + " has been closed.");

        return true;
    }
}
