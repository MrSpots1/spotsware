package com.yourname.temppunishplugin;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MuteManager {
    private final TempPunishPlugin plugin;
    private File muteFile;
    private FileConfiguration muteConfig;
    private final Map<UUID, Instant> mutedPlayers = new HashMap<>();

    public MuteManager(TempPunishPlugin plugin) {
        this.plugin = plugin;
        setupMuteFile();  // Load or create the mutes.yml file
        loadMutedPlayers();
    }

    private void setupMuteFile() {
        muteFile = new File(plugin.getDataFolder(), "mutes.yml");

        if (!muteFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs(); // Ensure folder exists
                muteFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create mutes.yml!");
                e.printStackTrace();
            }
        }

        muteConfig = YamlConfiguration.loadConfiguration(muteFile);  // Load YAML file
    }

    // Mute a player permanently
    public void mute(OfflinePlayer player) {
        mutedPlayers.put(player.getUniqueId(), Instant.MAX);
        saveMutedPlayers();
    }

    // Temporarily mute a player
    public void tempMute(OfflinePlayer player, long durationInSeconds) {
        Instant muteExpiry = Instant.now().plusSeconds(durationInSeconds);
        mutedPlayers.put(player.getUniqueId(), muteExpiry);
        saveMutedPlayers();
    }

    // Unmute a player
    public void unmute(OfflinePlayer player) {
        mutedPlayers.remove(player.getUniqueId());
        saveMutedPlayers();
    }

    // Check if a player is muted
    public boolean isMuted(OfflinePlayer player) {
        if (!mutedPlayers.containsKey(player.getUniqueId())) return false;
        Instant expiry = mutedPlayers.get(player.getUniqueId());
        if (expiry.isBefore(Instant.now())) { // If mute expired
            mutedPlayers.remove(player.getUniqueId()); // Remove expired mute
            saveMutedPlayers();
            return false;
        }
        return true;
    }

    // Load muted players from mutes.yml
    public void loadMutedPlayers() {
        if (muteConfig.contains("mutedPlayers")) {
            for (String key : muteConfig.getConfigurationSection("mutedPlayers").getKeys(false)) {
                UUID playerUUID = UUID.fromString(key);
                long expiryTime = muteConfig.getLong("mutedPlayers." + key);
                Instant expiry = Instant.ofEpochSecond(expiryTime);
                mutedPlayers.put(playerUUID, expiry);
            }
        }
    }

    // Save muted players to mutes.yml
    public void saveMutedPlayers() {
        muteConfig.set("mutedPlayers", null); // Clear existing data

        for (UUID playerUUID : mutedPlayers.keySet()) {
            Instant expiry = mutedPlayers.get(playerUUID);
            muteConfig.set("mutedPlayers." + playerUUID.toString(), expiry.getEpochSecond());
        }

        try {
            muteConfig.save(muteFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save mutes.yml!");
            e.printStackTrace();
        }
    }
}
