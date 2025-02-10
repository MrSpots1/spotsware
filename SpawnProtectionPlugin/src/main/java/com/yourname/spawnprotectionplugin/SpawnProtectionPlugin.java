package com.yourname.spawnprotectionplugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

import java.util.HashMap;
import java.util.UUID;

public class SpawnProtectionPlugin extends JavaPlugin implements Listener {

    private final HashMap<UUID, UUID> protectedPlayers = new HashMap<>();
    private final HashMap<UUID, Long> protectionExpiry = new HashMap<>();
    private static final long GRACE_PERIOD = 30 * 1000; // 30 seconds in milliseconds

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = null;
        // Check if the victim has a killer
        if (victim.getKiller() != null) {
            killer = victim.getKiller();

            // Only set up protection if the killer is not the same as the victim (i.e., not self-inflicted)
            if (!victim.getUniqueId().equals(killer.getUniqueId())) {
                protectedPlayers.put(victim.getUniqueId(), killer.getUniqueId());
                protectionExpiry.put(victim.getUniqueId(), System.currentTimeMillis() + GRACE_PERIOD);

                killer.sendMessage("You cannot damage " + victim.getName() + " for 30 seconds!");
                victim.sendMessage("You are protected from " + killer.getName() + " for 30 seconds!");
            }
        }
        else {
            EntityDamageEvent damageEvent = victim.getLastDamageCause();
            if (damageEvent instanceof EntityDamageByEntityEvent) {
                EntityDamageByEntityEvent entityDamageEvent = (EntityDamageByEntityEvent) damageEvent;
                Entity damager = entityDamageEvent.getDamager();

                // Direct melee attack
                if (damager instanceof Player) {
                    killer = (Player) damager;
                }
                // Arrows
                else if (damager instanceof Arrow) {
                    Arrow arrow = (Arrow) damager;
                    if (arrow.getShooter() instanceof Player) {
                        killer = (Player) arrow.getShooter();
                    }
                }
                // Trident
                else if (damager instanceof Trident) {
                    Trident trident = (Trident) damager;
                    if (trident.getShooter() instanceof Player) {
                        killer = (Player) trident.getShooter();
                    }
                }
                // Firework rocket (shot from crossbow)
                else if (damager instanceof Firework) {
                    Firework firework = (Firework) damager;
                    if (firework.getShooter() instanceof Player) {
                        killer = (Player) firework.getShooter();
                    }
                }
                // Thrown potions
                else if (damager instanceof ThrownPotion) {
                    ThrownPotion potion = (ThrownPotion) damager;
                    if (potion.getShooter() instanceof Player) {
                        killer = (Player) potion.getShooter();
                    }
                }
                // TNT
                else if (damager instanceof TNTPrimed) {
                    TNTPrimed tnt = (TNTPrimed) damager;
                    if (tnt.getSource() instanceof Player) {
                        killer = (Player) tnt.getSource();
                    }
                }
            }
            if (killer != null)
            {
                if (!victim.getUniqueId().equals(killer.getUniqueId())) {
                    protectedPlayers.put(victim.getUniqueId(), killer.getUniqueId());
                    protectionExpiry.put(victim.getUniqueId(), System.currentTimeMillis() + GRACE_PERIOD);

                    killer.sendMessage("You cannot damage " + victim.getName() + " for 30 seconds!");
                    victim.sendMessage("You are protected from " + killer.getName() + " for 30 seconds!");
                }
            }
        }
        // If the player died due to environmental damage (no killer), no protection is set
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            UUID killerUUID = protectedPlayers.get(victim.getUniqueId());

            // If there's a killer (i.e., they were recently killed by someone)
            if (killerUUID != null) {
                Player killer = Bukkit.getPlayer(killerUUID);

                // If the protection time has not expired, cancel damage from the killer only
                if (System.currentTimeMillis() < protectionExpiry.get(victim.getUniqueId())) {
                    if (event instanceof EntityDamageByEntityEvent) {
                        Entity damager = ((EntityDamageByEntityEvent) event).getDamager();

                        // If the damager is the killer, cancel the damage
                        if (damager instanceof Player && ((Player) damager).getUniqueId().equals(killerUUID)) {
                            event.setCancelled(true);
                            ((Player) damager).sendMessage("You cannot damage " + victim.getName() + " yet!");
                        }
                        else {

                            if (damager instanceof Arrow)
                            {
                                ProjectileSource source = ((Arrow) damager).getShooter();
                                if (source instanceof Player)
                                {
                                    Player shooter = (Player) source;
                                    if (shooter.getUniqueId().equals(killerUUID))
                                    {
                                        event.setCancelled(true);
                                        shooter.sendMessage("You cannot damage " + victim.getName() + " yet!");
                                    }

                                }
                            }
                            if (damager instanceof Trident)
                            {
                                ProjectileSource source = ((Trident) damager).getShooter();
                                if (source instanceof Player)
                                {
                                    Player shooter = (Player) source;
                                    if (shooter.getUniqueId().equals(killerUUID))
                                    {
                                        event.setCancelled(true);
                                        shooter.sendMessage("You cannot damage " + victim.getName() + " yet!");
                                    }

                                }
                            }
                            if (damager instanceof ThrownPotion)
                            {
                                ProjectileSource source = ((ThrownPotion) damager).getShooter();
                                if (source instanceof Player)
                                {
                                    Player shooter = (Player) source;
                                    if (shooter.getUniqueId().equals(killerUUID))
                                    {
                                        event.setCancelled(true);
                                        shooter.sendMessage("You cannot damage " + victim.getName() + " yet!");
                                    }

                                }
                            }
                            if (damager instanceof Firework)
                            {
                                ProjectileSource source = ((Firework) damager).getShooter();
                                if (source instanceof Player)
                                {
                                    Player shooter = (Player) source;
                                    if (shooter.getUniqueId().equals(killerUUID))
                                    {
                                        event.setCancelled(true);
                                        shooter.sendMessage("You cannot damage " + victim.getName() + " yet!");
                                    }

                                }
                            }
                            if (damager instanceof TNTPrimed)
                            {
                                Entity source = ((TNTPrimed) damager).getSource();
                                if (source instanceof Player)
                                {
                                    Player shooter = (Player) source;
                                    if (shooter.getUniqueId().equals(killerUUID))
                                    {
                                        event.setCancelled(true);
                                        shooter.sendMessage("You cannot damage " + victim.getName() + " yet!");
                                    }

                                }
                            }

                        }
                    }
                } else {
                    // If protection has expired, remove protection data
                    protectedPlayers.remove(victim.getUniqueId());
                    protectionExpiry.remove(victim.getUniqueId());
                }
            }
        }
    }

}
