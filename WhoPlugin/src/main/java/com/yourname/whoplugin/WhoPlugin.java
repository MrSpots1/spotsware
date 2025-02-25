package com.yourname.whoplugin;

import org.bukkit.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
    private final Map<Player, ChatColor> playerColors = new HashMap<>();
    private final Map<Player, ChatColor> playerStyles = new HashMap<>();
    private Map<String, Location> teamSpawnPoints = new HashMap<>();

    @Override
    public void onEnable()
    {

        LuckPermsManager lpManager = new LuckPermsManager();
        lpManager.CreateGroup("developer", 3);
        lpManager.CreateGroup("ambassador", 2);
        lpManager.CreateGroup("loyal", 1);
        configManager = new DataManager(this);
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("profile").setExecutor(new WhoCommand(this,configManager));
        getCommand("playermodify").setExecutor(new PlayerModifyCommand(this,configManager));
        getCommand("addnew").setExecutor(new AddNewCommand(this,configManager));
        getCommand("set-pronouns").setExecutor(new SetPronounsCommand(this,configManager));
        getCommand("check-ip").setExecutor(new CheckIPCommand(this,configManager));
        getCommand("pay").setExecutor(new PayCommand(this,configManager));
        this.getCommand("namecolor").setExecutor(this);
        this.getCommand("namecolor").setTabCompleter(this);
        this.getCommand("namestyle").setExecutor(this);
        this.getCommand("namestyle").setTabCompleter(this);
        this.getCommand("teamspawn").setExecutor(this);
        config = configManager.getConfig();
        try {
            teamSpawnPoints = loadHashMapFromConfig(config, "teamspawns");
        } catch (Exception e)
        {

        }


    }
    public static HashMap<String, Location> loadHashMapFromConfig(FileConfiguration config, String path) {
        HashMap<String, Location> map = new HashMap<>();
        for (String key : config.getConfigurationSection(path).getKeys(false)) {
            String locationString = config.getString(path + "." + key);
            String[] parts = locationString.split(",");
            World world = Bukkit.getServer().getWorld(parts[0]); // Assuming world name is the first part
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            float pitch = Float.parseFloat(parts[5]);

            Location location = new Location(world, x, y, z, yaw, pitch);
            map.put(key, location);
        }
        return map;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("namecolor")) {
            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Usage: /namecolor <color>");
                return true;
            }

            String colorName = args[0].toUpperCase();
            try {
                ChatColor color = ChatColor.valueOf(colorName);
                if (!color.isColor()) {
                    player.sendMessage(ChatColor.RED + "Invalid color! Use a valid ChatColor.");
                    return true;
                }

                playerColors.put(player, color);
                updatePlayerName(player);
                player.sendMessage(ChatColor.GREEN + "Your name color has been changed to " + color + colorName + ChatColor.GREEN + "!");
                String path = player.getUniqueId() + ".color";
                configManager.getConfig().set(path, color.name());
                configManager.saveConfig();
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Invalid color! Use a valid ChatColor.");
            }
        } else if (command.getName().equalsIgnoreCase("namestyle")) {
            if (args.length != 1) {
                player.sendMessage(ChatColor.RED + "Usage: /namestyle <style>");
                return true;
            }

            String styleName = args[0].toUpperCase();
            if (styleName.equals("RESET")) {
                playerStyles.remove(player);
                updatePlayerName(player);
                player.sendMessage(ChatColor.GREEN + "Your name style has been reset!");
                return true;
            }

            try {
                ChatColor style = ChatColor.valueOf(styleName);
                if (!style.isFormat() || style.equals(ChatColor.MAGIC)) {
                    player.sendMessage(ChatColor.RED + "Invalid style! Use normal, italic, bold, or italic_bold.");
                    return true;
                }

                playerStyles.put(player, style);
                updatePlayerName(player);
                player.sendMessage(ChatColor.GREEN + "Your name style has been changed to " + style + styleName + ChatColor.GREEN + "!");
                String path = player.getUniqueId() + ".style";
                configManager.getConfig().set(path, style.name());
                configManager.saveConfig();
            } catch (IllegalArgumentException e) {
                player.sendMessage(ChatColor.RED + "Invalid style! Use reset, italic, bold, underline, or strikethrough.");
            }
        } else if (command.getName().equalsIgnoreCase("teamspawn"))
        {
            if (args.length != 5) {
                player.sendMessage(ChatColor.RED + "Usage: /teamspawn <teamname> <x> <y> <z> <world>");
                return true;
            }
            String teamName = args[0];
            double x, y, z;
            String worldName = args[4];
            try {
                x = Double.parseDouble(args[1]);
                y = Double.parseDouble(args[2]);
                z = Double.parseDouble(args[3]);
            } catch (NumberFormatException e) {
                player.sendMessage(ChatColor.RED + "Invalid coordinates.");
                return true;
            }
            World world = player.getServer().getWorld(worldName);
            if (world == null) {
                player.sendMessage(ChatColor.RED + "World not found.");
                return true;
            }
            Location location = new Location(world, x, y, z);
            teamSpawnPoints.put(teamName, location);

        }
        sender.sendMessage(ChatColor.GREEN + "Team Spawn Updated!");
        return true;
    }
    public void setTeamSpawnPoint(String team, Location location) {
        teamSpawnPoints.put(team, location);
    }


    private void updatePlayerName(Player player) {
        ChatColor color = playerColors.getOrDefault(player, ChatColor.WHITE);
        ChatColor style = playerStyles.get(player); // Don't default to RESET here

        String formattedName = color.toString() + (style != null ? style.toString() : "") + player.getName() + ChatColor.RESET;
        player.setDisplayName(formattedName);
        player.setPlayerListName(formattedName);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            if (command.getName().equalsIgnoreCase("namecolor")) {
                return Arrays.asList("Aqua", "Black", "Blue", "Dark_aqua", "Dark_blue", "Dark_grey", "Dark_green", "Dark_purple", "Dark_red", "Gold", "Gray", "Green", "Light_purple", "Red", "White", "Yellow");
            } else if (command.getName().equalsIgnoreCase("namestyle")) {
                return Arrays.asList("Bold", "Italic", "Reset", "Underline", "Strikethrough");
            }
        }
        return Collections.emptyList();
    }
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        if (player.getBedSpawnLocation() == null) {
            String team = configManager.getConfig().getString(playerUUID + ".school");

                Location teamSpawnPoint = teamSpawnPoints.get(team);
                if (teamSpawnPoint != null) {
                    event.setRespawnLocation(teamSpawnPoint);
                }

        }
    }
    @EventHandler
    public void onLogOn(PlayerJoinEvent event)
    {

        String ip = event.getPlayer().getAddress().getAddress().getHostAddress();
        String path;
        path = event.getPlayer().getUniqueId() + ".ip";
        configManager.getConfig().set(path, ip);
        configManager.saveConfig();
        path = event.getPlayer().getUniqueId() + ".color";
        String color = configManager.getConfig().getString(path);
        path = event.getPlayer().getUniqueId() + ".style";
        String style = configManager.getConfig().getString(path);
        playerColors.put(event.getPlayer(), ChatColor.valueOf(color));
        playerStyles.put(event.getPlayer(), ChatColor.valueOf(style));
        updatePlayerName(event.getPlayer());


    }
    private String locationToString(Location location) {
        return location.getWorld().getName() + "," +
                location.getX() + "," +
                location.getY() + "," +
                location.getZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }
    @Override
    public void onDisable() {
        Set<String> keys = teamSpawnPoints.keySet();
        for (String key : keys) {
            Location location = teamSpawnPoints.get(key);
            // Convert Location to a string (world, x, y, z, yaw, pitch)
            String locationString = location.getWorld().getName() + "," +
                    location.getX() + "," +
                    location.getY() + "," +
                    location.getZ() + "," +
                    location.getYaw() + "," +
                    location.getPitch();
            // Save to config
            configManager.getConfig().set("teamspawns" + "." + key, locationString);
            configManager.saveConfig();
        }

    }

}
