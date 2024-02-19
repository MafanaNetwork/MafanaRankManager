package me.tahacheji.mafana.rankManager;

import me.tahacheji.mafana.MafanaRankManager;
import me.tahacheji.mafana.commandExecutor.paramter.impl.ChatColorProcessor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Rank implements RankEvents {
    private String rankID;
    private String rankDisplayName;
    private int rankPriority;
    private List<RankPermission> rankPermissionList;

    public Rank(String rankID, String rankDisplayName, int rankPriority, List<RankPermission> rankPermissionList) {
        this.rankID = rankID;
        this.rankDisplayName = rankDisplayName;
        this.rankPriority = rankPriority;
        this.rankPermissionList = rankPermissionList;
    }

    public Rank(String rankID, String rankDisplayName, int rankPriority, RankPermission... rankPermissionList) {
        this.rankID = rankID;
        this.rankDisplayName = rankDisplayName;
        this.rankPriority = rankPriority;
        this.rankPermissionList = Arrays.asList(rankPermissionList);
    }

    public CompletableFuture<Void> registerPlayerWithRank(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                net.md_5.bungee.api.ChatColor chatColor = MafanaRankManager.getInstance().getPlayerRankDatabase().extractChatColor(player.getUniqueId()).get();
                List<RankPermission> rankPermissions = MafanaRankManager.getInstance().getPlayerRankDatabase().getPermissions(player.getUniqueId()).get();
                String name = rankDisplayName + " " + player.getName();
                player.setDisplayName(name);
                player.setPlayerListName(name);
                Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                Team team = scoreboard.getTeam(rankID);
                if (team == null) {
                    team = scoreboard.registerNewTeam(rankID);
                }
                if(chatColor != null) {
                    team.setPrefix(rankDisplayName + " " + MafanaRankManager.getInstance().getPlayerRankDatabase().extractChatColor(player.getUniqueId()));
                } else {
                    team.setPrefix(rankDisplayName + " ");
                }
                team.addPlayer(player);

                for (RankPermission rankPermission : rankPermissionList) {
                    Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(rankPermission.getPlugin());
                    if (plugin != null) {
                        PermissionAttachment x = player.addAttachment(plugin);
                        x.setPermission(rankPermission.getPermission(), true);
                    }
                }
                if (rankPermissions != null) {
                    for (RankPermission rankPermission : rankPermissions) {
                        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(rankPermission.getPlugin());
                        if (plugin != null) {
                            PermissionAttachment x = player.addAttachment(plugin);
                            x.setPermission(rankPermission.getPermission(), true);
                        }
                    }
                }
                player.recalculatePermissions();
                player.updateCommands();
                return null;
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public ChatColor convertToBukkitChatColor(net.md_5.bungee.api.ChatColor bungeeChatColor) {
        String code = bungeeChatColor.toString();
        return ChatColor.getByChar(code.charAt(1));
    }

    public void setRankID(String rankID) {
        this.rankID = rankID;
    }

    public void setRankDisplayName(String rankDisplayName) {
        this.rankDisplayName = rankDisplayName;
    }

    public void setRankPriority(int rankPriority) {
        this.rankPriority = rankPriority;
    }

    public void setRankPermissionList(List<RankPermission> rankPermissionList) {
        this.rankPermissionList = rankPermissionList;
    }

    public String getRankID() {
        return rankID;
    }

    public String getRankDisplayName() {
        return rankDisplayName;
    }

    public int getRankPriority() {
        return rankPriority;
    }

    public List<RankPermission> getRankPermissionList() {
        return rankPermissionList;
    }
}
