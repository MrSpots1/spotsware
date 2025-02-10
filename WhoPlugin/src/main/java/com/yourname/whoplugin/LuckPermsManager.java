package com.yourname.whoplugin;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
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
