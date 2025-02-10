package com.yourname.playerreportplugin;

public class Report {

    private static int reportCount = 0;
    private final String reportID;
    private final String reporterName;
    private final String reportedPlayerName;
    private final String reason;

    public Report(String reporterName, String reportedPlayerName, String reason) {
        this.reportID = "report_" + (++reportCount); // Unique ID for the report
        this.reporterName = reporterName;
        this.reportedPlayerName = reportedPlayerName;
        this.reason = reason;
    }

    public String getReportID() {
        return reportID;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getReportedPlayerName() {
        return reportedPlayerName;
    }

    public String getReason() {
        return reason;
    }

    // Convert report to string format for saving
    @Override
    public String toString() {
        return reportID + "," + reporterName + "," + reportedPlayerName + "," + reason;
    }

    // Convert string back to report object when loading
    public static Report fromString(String reportStr) {
        String[] parts = reportStr.split(",");
        if (parts.length != 4) {
            return null; // Invalid format
        }
        return new Report(parts[1], parts[2], parts[3]); // Create new Report object
    }
}
