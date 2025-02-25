package com.yourname.damagecontrolplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class DataManager {
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;
    public DataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadConfig();
    }
    public void loadConfig() {
        configFile = new File(plugin.getDataFolder(), "profiles.yml");
        if (!configFile.exists()) {
            plugin.saveResource("profiles.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    public void saveConfig() {
        if (config == null || configFile == null) return;
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save profiles.yml!");
            e.printStackTrace();

        }
    }
    public FileConfiguration getConfig() {
        return config;
    }
}
