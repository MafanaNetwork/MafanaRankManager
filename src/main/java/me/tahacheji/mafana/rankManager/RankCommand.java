package me.tahacheji.mafana.rankManager;

import me.tahacheji.mafana.MafanaRankManager;
import me.tahacheji.mafana.commandExecutor.Command;
import me.tahacheji.mafana.commandExecutor.paramter.Param;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RankCommand {

    @Command(names = {"mrm setRank", "mafanarank setRank", "mafanarankmanager setRank"}, permission = "mafana.admin", playerOnly = false)
    public void setPlayerRank(CommandSender sender, @Param(name = "player") OfflinePlayer target, @Param(name = "rankID", concated = true) Rank rankID) {
        CompletableFuture<Rank> z = MafanaRankManager.getInstance().getRankDatabase().getRank(rankID.getRankID());
        z.thenAcceptAsync(rank -> {
            if (rank != null) {
                MafanaRankManager.getInstance().getPlayerRankDatabase().setPlayerRank(target.getUniqueId(), rankID.getRankID());
                sender.sendMessage(ChatColor.GREEN + "Rank updated successfully from player: " + target.getName());
            } else {
                sender.sendMessage(ChatColor.RED + "Rank not found.");
            }
        });
    }

    @Command(names = {"mrm addRankPermission", "mafanarank addRankPermission", "mafanarankmanager addRankPermission"}, permission = "mafana.admin", playerOnly = false)
    public void addRankPermission(CommandSender sender, @Param(name = "rankID") String rankID, @Param(name = "plugin") String plugin, @Param(name = "permission") String permission) {
        CompletableFuture<Rank> z = MafanaRankManager.getInstance().getRankDatabase().getRank(rankID);
        z.thenAcceptAsync(rank -> {
            if (rank == null) {
                sender.sendMessage(ChatColor.RED + "Rank not found.");
                return;
            }
            List<RankPermission> rankPermissionList = rank.getRankPermissionList();
            rankPermissionList.add(new RankPermission(plugin, permission));
            rank.setRankPermissionList(rankPermissionList);
            MafanaRankManager.getInstance().getRankDatabase().setRank(rank);
            sender.sendMessage(ChatColor.GREEN + "Rank permission updated successfully for rank: " + rank.getRankDisplayName());
        });
    }

    @Command(names = {"mrm removeRankPermission", "mafanarank removeRankPermission", "mafanarankmanager removeRankPermission"}, permission = "mafana.admin", playerOnly = false)
    public void removeRankPermission(CommandSender sender, @Param(name = "rankID") String rankID, @Param(name = "plugin") String plugin, @Param(name = "permission") String permission) {
        CompletableFuture<Rank> z = MafanaRankManager.getInstance().getRankDatabase().getRank(rankID);
        z.thenAcceptAsync(rank -> {
            if (rank == null) {
                sender.sendMessage(ChatColor.RED + "Rank not found.");
                return;
            }

            List<RankPermission> rankPermissionList = rank.getRankPermissionList();
            RankPermission permissionToRemove = null;

            for (RankPermission rankPermission : rankPermissionList) {
                if (rankPermission.getPlugin().equals(plugin) && rankPermission.getPermission().equals(permission)) {
                    permissionToRemove = rankPermission;
                    break;
                }
            }

            if (permissionToRemove != null) {
                rankPermissionList.remove(permissionToRemove);
                rank.setRankPermissionList(rankPermissionList);
                MafanaRankManager.getInstance().getRankDatabase().setRank(rank);
                sender.sendMessage(ChatColor.GREEN + "Rank permission updated successfully for rank: " + rank.getRankDisplayName());
            } else {
                sender.sendMessage(ChatColor.RED + "Rank permission error for rank: " + rank.getRankDisplayName());
            }
        });
    }
}
