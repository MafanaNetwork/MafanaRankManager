package me.tahacheji.mafana.rankManager;

import me.tahacheji.mafana.MafanaRankManager;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PlayerRankCommand {

    @Command(names = {"mrm addPlayerPermission", "mafanarank addPlayerPermission", "mafanarankmanager addPlayerPermission"}, permission = "mafana.admin", playerOnly = false)
    public void addPlayerPermission(CommandSender sender, @Param(name = "player") OfflinePlayer target, @Param(name = "plugin") String plugin, @Param(name = "permission") String permission) {
        CompletableFuture<List<RankPermission>> permissionsFuture = MafanaRankManager.getInstance().getPlayerRankDatabase().getPermissions(target.getUniqueId());
        permissionsFuture.thenAcceptAsync(rankPermissionList -> {
            try {
                if (rankPermissionList == null) {
                    rankPermissionList = new ArrayList<>();
                }
                rankPermissionList.add(new RankPermission(plugin, permission));
                CompletableFuture<Void> setPermissionsFuture = MafanaRankManager.getInstance().getPlayerRankDatabase().setPermissions(target.getUniqueId(), rankPermissionList);
                setPermissionsFuture.thenRunAsync(() -> {
                    sender.sendMessage(ChatColor.GREEN + "Permission updated successfully from player: " + target.getName());
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    sender.sendMessage(ChatColor.RED + "An error occurred while updating permissions for player: " + target.getName());
                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "An error occurred while updating permissions for player: " + target.getName());
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            sender.sendMessage(ChatColor.RED + "An error occurred while retrieving permissions for player: " + target.getName());
            return null;
        });
    }



    @Command(names = {"mrm removePlayerPermission", "mafanarank removePlayerPermission", "mafanarankmanager removePlayerPermission"}, permission = "mafana.admin", playerOnly = false)
    public void removePlayerPermission(CommandSender sender, @Param(name = "player") OfflinePlayer target, @Param(name = "plugin") String plugin, @Param(name = "permission") String permission) {
        CompletableFuture<List<RankPermission>> permissionsFuture = MafanaRankManager.getInstance().getPlayerRankDatabase().getPermissions(target.getUniqueId());
        permissionsFuture.thenAcceptAsync(rankPermissionList -> {
            try {
                if (rankPermissionList == null) {
                    rankPermissionList = new ArrayList<>();
                }
                RankPermission permissionToRemove = null;

                for (RankPermission playerPermission : rankPermissionList) {
                    if (playerPermission.getPlugin().equals(plugin) && playerPermission.getPermission().equals(permission)) {
                        permissionToRemove = playerPermission;
                        break;
                    }
                }

                if (permissionToRemove != null) {
                    rankPermissionList.remove(permissionToRemove);
                    CompletableFuture<Void> setPermissionsFuture = MafanaRankManager.getInstance().getPlayerRankDatabase().setPermissions(target.getUniqueId(), rankPermissionList);
                    setPermissionsFuture.thenRunAsync(() -> {
                        sender.sendMessage(ChatColor.GREEN + "Permission removed successfully from player: " + target.getName());
                    }).exceptionally(ex -> {
                        ex.printStackTrace();
                        sender.sendMessage(ChatColor.RED + "An error occurred while removing permissions for player: " + target.getName());
                        return null;
                    });
                } else {
                    sender.sendMessage(ChatColor.RED + "Permission not found for player: " + target.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
                sender.sendMessage(ChatColor.RED + "An error occurred while removing permissions for player: " + target.getName());
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            sender.sendMessage(ChatColor.RED + "An error occurred while retrieving permissions for player: " + target.getName());
            return null;
        });
    }


    @Command(names = {"mrm setColor", "mafanarank setColor", "mafanarankmanager setColor"}, permission = "mafana.admin", playerOnly = false)
    public void changePlayerColor(CommandSender player, @Param(name = "target") OfflinePlayer target, @Param(name = "red") int r, @Param(name = "blue") int b, @Param(name = "green") int g) {
        CompletableFuture<Void> setColorFuture = CompletableFuture.runAsync(() -> {
            try {
                net.md_5.bungee.api.ChatColor chatColor = net.md_5.bungee.api.ChatColor.of(new Color(r, b, g));
                String colorString = chatColor + "";
                CompletableFuture<Void> setColorResultFuture = MafanaRankManager.getInstance().getPlayerRankDatabase().setColor(target.getUniqueId(), colorString);
                setColorResultFuture.thenRun(() -> {
                    player.sendMessage(ChatColor.GREEN + "Color updated successfully for player: " + target.getName());
                }).exceptionally(ex -> {
                    ex.printStackTrace();
                    player.sendMessage(ChatColor.RED + "An error occurred while updating color for player: " + target.getName());
                    return null;
                });
            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "An error occurred while updating color for player: " + target.getName());
            }
        });
    }


}
