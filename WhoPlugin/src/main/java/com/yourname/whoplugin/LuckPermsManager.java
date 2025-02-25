package com.yourname.whoplugin;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LuckPermsManager {

    private final LuckPerms luckPerms;

    public LuckPermsManager() {
        this.luckPerms = LuckPermsProvider.get();
    }

    public void addOfflinePlayerToGroup(UUID uuid, String groupName) {
        CompletableFuture.runAsync(() -> {
            luckPerms.getUserManager().loadUser(uuid).thenAccept(user -> {
                if (user != null) {
                    user.data().add(Node.builder("group." + groupName).build());
                    luckPerms.getUserManager().saveUser(user);

                }
            });
        });
    }
    public void CreateGroup(String groupName, int groupnumber)
    {
        if (luckPerms.getGroupManager().getGroup(groupName) == null) {
            Group group = luckPerms.getGroupManager().createAndLoadGroup(groupName).join();

            if (group != null) {
                if (groupnumber == 1) {
                    group.data().add(Node.builder("huskhomes.max_homes.15").build());
                    group.data().add(Node.builder("whoplugin.customize").build());
                    group.data().add(Node.builder("whoplugin.pay").build());


                    // Save changes
                    luckPerms.getGroupManager().saveGroup(group);
                }
                else if (groupnumber == 2) {
                    group.data().add(Node.builder("playerreportplugin.managereports").build());
                    group.data().add(Node.builder("temppunishplugin.givepunishments").build());
                    group.data().add(Node.builder("minecraft.command.whitelist").build());
                    group.data().add(Node.builder("minecraft.command.pardon-ip").build());
                    group.data().add(Node.builder("minecraft.command.pardon").build());
                    group.data().add(Node.builder("minecraft.command.kick").build());
                    group.data().add(Node.builder("minecraft.command.banlist").build());
                    group.data().add(Node.builder("minecraft.command.ban-ip").build());
                    group.data().add(Node.builder("minecraft.command.ban").build());


                    // Save changes
                    luckPerms.getGroupManager().saveGroup(group);
                }
                else if (groupnumber == 3) {
                    group.data().add(Node.builder("playerreportplugin.managereports").build());
                    group.data().add(Node.builder("temppunishplugin.givepunishments").build());
                    group.data().add(Node.builder("minecraft.command.whitelist").build());
                    group.data().add(Node.builder("minecraft.command.pardon-ip").build());
                    group.data().add(Node.builder("minecraft.command.pardon").build());
                    group.data().add(Node.builder("minecraft.command.kick").build());
                    group.data().add(Node.builder("minecraft.command.banlist").build());
                    group.data().add(Node.builder("minecraft.command.ban-ip").build());
                    group.data().add(Node.builder("minecraft.command.ban").build());
                    group.data().add(Node.builder("minecraft.command.reload").build());
                    group.data().add(Node.builder("luckperms.*").build());
                    group.data().add(Node.builder("whoplugin.manageprofiles").build());
                    group.data().add(Node.builder("minecraft.commandblock").build());
                    group.data().add(Node.builder("minecraft.command.scoreboard").build());
                    group.data().add(Node.builder("minecraft.command.reload").build());
                    group.data().add(Node.builder("huskhomes.max_homes.15").build());
                    group.data().add(Node.builder("whoplugin.customize").build());
                    group.data().add(Node.builder("whoplugin.pay").build());
                    group.data().add(Node.builder("worldguard.*").build());
                    group.data().add(Node.builder("worldedit.wand").build());


                    // Save changes
                    luckPerms.getGroupManager().saveGroup(group);
                }

            }
        }


    }
    public void removeOfflinePlayerFromGroup(UUID uuid, String groupName) {
        CompletableFuture.runAsync(() -> {
            luckPerms.getUserManager().loadUser(uuid).thenAccept(user -> {
                if (user != null) {
                    user.data().remove(Node.builder("group." + groupName).build());
                    luckPerms.getUserManager().saveUser(user);
                }
            });
        });
    }
}
