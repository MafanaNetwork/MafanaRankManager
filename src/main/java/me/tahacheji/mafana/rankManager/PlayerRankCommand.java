package me.tahacheji.mafana.rankManager;

import me.tahacheji.mafana.MafanaRankManager;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class PlayerRankCommand {

    @Command(names = {"mrm addPlayerPermission", "mafanarank addPlayerPermission", "mafanarankmanager addPlayerPermission"}, permission = "mafana.admin", playerOnly = false)
    public void addPlayerPermission(CommandSender sender, @Param(name = "player") OfflinePlayer target, @Param(name = "plugin") String plugin, @Param(name = "permission") String permission) {
        try {
            List<RankPermission> rankPermissionList;
            if(MafanaRankManager.getInstance().getPlayerRankDatabase().getPermissions(target.getUniqueId()) != null) {
                rankPermissionList = MafanaRankManager.getInstance().getPlayerRankDatabase().getPermissions(target.getUniqueId());
            } else {
                rankPermissionList = new ArrayList<>();
            }
            rankPermissionList.add(new RankPermission(plugin, permission));
            MafanaRankManager.getInstance().getPlayerRankDatabase().setPermissions(target.getUniqueId(), rankPermissionList);
            sender.sendMessage(ChatColor.GREEN + "Permission updated successfully from player: " + target.getName());
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            sender.sendMessage(ChatColor.RED + "An error occurred while updating permissions for player: " + target.getName());
        }
    }


    @Command(names = {"mrm removePlayerPermission", "mafanarank removePlayerPermission", "mafanarankmanager removePlayerPermission"}, permission = "mafana.admin", playerOnly = false)
    public void removePlayerPermission(CommandSender sender, @Param(name = "player") OfflinePlayer target, @Param(name = "plugin") String plugin, @Param(name = "permission") String permission) {
        List<RankPermission> rankPermissionList;
        if(MafanaRankManager.getInstance().getPlayerRankDatabase().getPermissions(target.getUniqueId()) != null) {
            rankPermissionList = MafanaRankManager.getInstance().getPlayerRankDatabase().getPermissions(target.getUniqueId());
        } else {
            rankPermissionList = new ArrayList<>();
        }
        RankPermission permissionToRemove = null;

        // Iterate through the player's permissions to find and remove the specified permission
        for (RankPermission playerPermission : rankPermissionList) {
            if (playerPermission.getPlugin().equals(plugin) && playerPermission.getPermission().equals(permission)) {
                permissionToRemove = playerPermission;
                break;
            }
        }

        if (permissionToRemove != null) {
            rankPermissionList.remove(permissionToRemove);
            MafanaRankManager.getInstance().getPlayerRankDatabase().setPermissions(target.getUniqueId(), rankPermissionList);
            sender.sendMessage(ChatColor.GREEN + "Permission removed successfully from player: " + target.getName());
        } else {
            sender.sendMessage(ChatColor.RED + "Permission not found for player: " + target.getName());
        }
    }

}
