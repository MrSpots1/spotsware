package com.yourname.whoplugin;

import org.bukkit.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

public class WhoPlugin extends JavaPlugin implements Listener {
    private File file;
    private FileConfiguration config;
    private DataManager configManager;
    private static final Logger GLOBAL_LOGGER = Logger.getLogger(""); // Root logger
    private static PrintStream originalOut;
    private static PrintStream originalErr;
    @Override
    public void onEnable()
    {

        configManager = new DataManager(this);
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("profile").setExecutor(new WhoCommand(this,configManager));
        getCommand("playermodify").setExecutor(new PlayerModifyCommand(this,configManager));
        getCommand("addnew").setExecutor(new AddNewCommand(this,configManager));
        getCommand("set-pronouns").setExecutor(new SetPronounsCommand(this,configManager));
        getCommand("check-ip").setExecutor(new CheckIPCommand(this,configManager));


    }

    @EventHandler
    public void onLogOn(PlayerJoinEvent event)
    {

        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();
        String path;
        path = event.getPlayer().getUniqueId() + ".ip";
        configManager.getConfig().set(path, ip);
        configManager.saveConfig();

    }
    @Override
    public void onDisable() {

    }

}
