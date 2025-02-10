package com.yourname.whoplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class CheckIPCommand implements CommandExecutor {
    private final WhoPlugin plugin;
    private final DataManager configManager;
    public CheckIPCommand(WhoPlugin plugin, DataManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
        {
            OfflinePlayer target;
            Player onlineTarget = sender.getServer().getPlayer(args[0]);


            if (onlineTarget != null) {
                target = onlineTarget; // Assign online player to target
            } else {
                // Try to get offline player
                target = sender.getServer().getOfflinePlayer(args[0]);

                if (target == null || !target.hasPlayedBefore()) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return true;
                }
            }
            String targetuu = target.getUniqueId().toString();
            String targetpath = targetuu + ".ip";
            String targetinfo = configManager.getConfig().getString(targetpath);
            List<String> matches = new List<String>() {
                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean contains(Object o) {
                    return false;
                }

                @Override
                public @NotNull Iterator<String> iterator() {
                    return null;
                }

                @Override
                public @NotNull Object[] toArray() {
                    return new Object[0];
                }

                @Override
                public @NotNull <T> T[] toArray(@NotNull T[] a) {
                    return null;
                }

                @Override
                public boolean add(String s) {
                    return false;
                }

                @Override
                public boolean remove(Object o) {
                    return false;
                }

                @Override
                public boolean containsAll(@NotNull Collection<?> c) {
                    return false;
                }

                @Override
                public boolean addAll(@NotNull Collection<? extends String> c) {
                    return false;
                }

                @Override
                public boolean addAll(int index, @NotNull Collection<? extends String> c) {
                    return false;
                }

                @Override
                public boolean removeAll(@NotNull Collection<?> c) {
                    return false;
                }

                @Override
                public boolean retainAll(@NotNull Collection<?> c) {
                    return false;
                }

                @Override
                public void clear() {

                }

                @Override
                public String get(int index) {
                    return "";
                }

                @Override
                public String set(int index, String element) {
                    return "";
                }

                @Override
                public void add(int index, String element) {

                }

                @Override
                public String remove(int index) {
                    return "";
                }

                @Override
                public int indexOf(Object o) {
                    return 0;
                }

                @Override
                public int lastIndexOf(Object o) {
                    return 0;
                }

                @Override
                public @NotNull ListIterator<String> listIterator() {
                    return null;
                }

                @Override
                public @NotNull ListIterator<String> listIterator(int index) {
                    return null;
                }

                @Override
                public @NotNull List<String> subList(int fromIndex, int toIndex) {
                    return List.of();
                }
            };
            for (OfflinePlayer player : Bukkit.getOfflinePlayers())
            {
                if (player.getUniqueId().toString() != targetuu)
                {
                    String path = player.getUniqueId() + ".ip";
                    String info = configManager.getConfig().getString(path);
                    if (info == targetinfo)
                    {
                        matches.add(player.getName());
                    }
                }
            }
            if (matches.size() == 0)
            {
                sender.sendMessage(ChatColor.RED + "No IP matches.");
            }
            else
            {
                sender.sendMessage(ChatColor.GREEN + "The following players have the same IP: " + matches);
            }

            return true;
        }
        return false;
    }
}