package com.yourname.playerreportplugin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportManager {

    private final JavaPlugin plugin;
    private final List<Report> reports;
    private File reportFile;
    private FileConfiguration reportConfig;

    public ReportManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.reports = new ArrayList<>();
        createReportFile();
        loadReports();
    }
    private void createReportFile() {
        reportFile = new File(plugin.getDataFolder(), "reports.yml");

        if (!reportFile.exists()) {
            try {
                reportFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create reports.yml!");
            }
        }

        reportConfig = YamlConfiguration.loadConfiguration(reportFile);
    }
    // Add a report
    public void addReport(Report report) {
        reports.add(report);
        saveReports();
    }

    // Get a list of all open reports
    public List<Report> getReports() {
        return reports;
    }

    // Get a report by its unique ID
    public Report getReportByID(String reportID) {
        for (Report report : reports) {
            if (report.getReportID().equals(reportID)) {
                return report;
            }
        }
        return null;  // Report not found
    }

    // Remove a report
    public void removeReport(Report report) {
        reports.remove(report);
        saveReports();
    }

    // Save reports to a YAML file
    public void saveReports() {
        List<String> reportList = new ArrayList<>();
        for (Report report : reports) {
            reportList.add(report.toString()); // Convert report object to string format
        }
        reportConfig.set("reports", reportList);
        try {
            reportConfig.save(reportFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save reports.yml!");
        }
    }

    // Load reports from a YAML file
    private void loadReports() {
        List<String> reportList = reportConfig.getStringList("reports");
        for (String reportStr : reportList) {
            Report report = Report.fromString(reportStr);
            reports.add(report);
        }
    }
}
