package com.yourname.damagecontrolplugin;

import org.bukkit.*;
import java.io.File;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageControlPlugin extends JavaPlugin implements Listener {
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
        getCommand("toggledamage").setExecutor(new ToggleDamageCommand(this,configManager));



    }
    @EventHandler
    public void Damage(EntityDamageByEntityEvent event)
    {
        if (event.getEntity() instanceof Player)
        {

            Player player = (Player) event.getEntity();
            Entity damager = event.getDamager();

            if (damager instanceof Player)
            {

                Player playerdamager = (Player) damager;
                ItemStack item = playerdamager.getInventory().getItemInMainHand();

                if (item.getType() == Material.MACE)
                {
                    if (check(configManager.getConfig().get("damage.mace").toString()))
                    {

                        event.setDamage(0);
                        player.sendMessage(ChatColor.GREEN + "You have been saved from a mace attack!");

                    }
                }

            }
            else if (damager instanceof ExplosiveMinecart)
            {

                ExplosiveMinecart cart = (ExplosiveMinecart) damager;

                    if (check(configManager.getConfig().get("damage.cart").toString()))
                    {

                        event.setDamage(0);
                        player.sendMessage(ChatColor.GREEN + "You have been saved from a tnt minecart!");

                    }


            }
            else if (damager instanceof EnderCrystal)
            {

                EnderCrystal crystal = (EnderCrystal) damager;

                if (check(configManager.getConfig().get("damage.crystal").toString()))
                {

                    event.setDamage(0);

                    player.sendMessage(ChatColor.GREEN + "You have been saved from an ender crystal!");

                }


            }

        }

    }
    public boolean check(String str)
    {
        if (str.equals("true"))
        {
            return true;
        }
        else return false;
    }
    @Override
    public void onDisable() {


    }

}
