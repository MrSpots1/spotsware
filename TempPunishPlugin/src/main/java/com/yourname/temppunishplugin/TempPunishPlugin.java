package com.yourname.temppunishplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class TempPunishPlugin extends JavaPlugin {
    private static TempPunishPlugin instance;
    private MuteManager muteManager;

    public static TempPunishPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        muteManager = new MuteManager(this);

        // Load muted players from the config
        muteManager.loadMutedPlayers();

        // Register commands
        getCommand("mute").setExecutor(new MuteCommand(muteManager));
        getCommand("unmute").setExecutor(new UnmuteCommand(muteManager));
        getCommand("tempban").setExecutor(new TempBanCommand());
        getCommand("tempip-ban").setExecutor(new TempIPBanCommand());
        getCommand("tempmute").setExecutor(new TempMuteCommand(muteManager));

        // Register the listener to block muted player actions
        getServer().getPluginManager().registerEvents(new MuteListener(muteManager), this);

        getLogger().info("TempPunishPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // Save muted players to the config
        muteManager.saveMutedPlayers();
        getLogger().info("TempPunishPlugin has been disabled.");
    }
}
