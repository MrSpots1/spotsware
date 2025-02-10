package com.yourname.playerreportplugin;

import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerReportPlugin extends JavaPlugin {

    private ReportManager reportManager;

    @Override
    public void onEnable() {
        getLogger().info("PlayerReportPlugin is being enabled...");

        // Initialize the ReportManager and load reports from the config
        try {
            reportManager = new ReportManager(this);
            getLogger().info("ReportManager initialized.");
        } catch (Exception e) {
            getLogger().severe("Error initializing ReportManager: " + e.getMessage());
            e.printStackTrace();
        }

        // Register commands
        registerCommand("report", new ReportCommand(reportManager));
        registerCommand("viewreports", new ViewReportsCommand(reportManager));
        registerCommand("closereports", new CloseReportCommand(reportManager));
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        if (getCommand(commandName) != null) {
            getCommand(commandName).setExecutor(executor);
            getLogger().info("Command '/" + commandName + "' registered successfully.");
        } else {
            getLogger().severe("Error: Command '/" + commandName + "' is not defined in plugin.yml or the command name is incorrect.");
        }
    }


    @Override
    public void onDisable() {
        // Save reports before disabling
        reportManager.saveReports();
    }

    public ReportManager getReportManager() {
        return reportManager;
    }
}
