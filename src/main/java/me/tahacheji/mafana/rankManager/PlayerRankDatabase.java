package me.tahacheji.mafana.rankManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.tahacheji.mafana.MafanaRankManager;
import me.tahacheji.mafana.data.DatabaseValue;
import me.tahacheji.mafana.data.MySQL;
import me.tahacheji.mafana.data.SQLGetter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerRankDatabase extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public PlayerRankDatabase() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public CompletableFuture<Void> setPlayerRank(UUID offlinePlayer, String id) {
        return MafanaRankManager.getInstance().getRankDatabase().getRank(id)
                .thenComposeAsync(rank -> {
                    if (rank != null) {
                        return sqlGetter.setStringAsync(new DatabaseValue("NAME", offlinePlayer, Bukkit.getOfflinePlayer(offlinePlayer).getName()))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("RANK_ID", offlinePlayer, id)))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("COLOR", offlinePlayer, "")))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("PERMISSIONS", offlinePlayer, "")));
                    }
                    return CompletableFuture.completedFuture(null);
                });
    }


    public CompletableFuture<Rank> getPlayerRank(UUID offlinePlayer) {
        return sqlGetter.existsAsync(offlinePlayer)
                .thenComposeAsync(exists -> {
                    if (exists) {
                        return sqlGetter.getStringAsync(offlinePlayer, new DatabaseValue("RANK_ID"))
                                .thenComposeAsync(rankId -> {
                                    try {
                                        if (rankId != null) {
                                            return MafanaRankManager.getInstance().getRankDatabase().getRank(rankId);
                                        } else {
                                            // Return null if the rank ID is null
                                            return CompletableFuture.completedFuture(null);
                                        }
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    } else {
                        // Return null if the player's rank doesn't exist
                        return CompletableFuture.completedFuture(null);
                    }
                });
    }


    public CompletableFuture<Void> setColor(UUID uuid, String color) {
        return sqlGetter.existsAsync(uuid)
                .thenComposeAsync(exists -> {
                    if (exists) {
                        return sqlGetter.setStringAsync(new DatabaseValue("COLOR", uuid, color));
                    } else {
                        // Return a completed future with null if the UUID doesn't exist
                        return CompletableFuture.completedFuture(null);
                    }
                });
    }

    public CompletableFuture<String> getColor(UUID uuid) {
        return sqlGetter.existsAsync(uuid)
                .thenComposeAsync(exists -> {
                    if (exists) {
                        return sqlGetter.getStringAsync(uuid, new DatabaseValue("COLOR"));
                    } else {
                        // Return a completed future with null if the UUID doesn't exist
                        return CompletableFuture.completedFuture(null);
                    }
                });
    }


    public CompletableFuture<ChatColor> extractChatColor(UUID uuid) {
        return getColor(uuid).thenApplyAsync(colorString -> {
            if (colorString != null) {
                Pattern pattern = Pattern.compile("(?i)(&|§)([0-9a-fk-or])");
                Matcher matcher = pattern.matcher(colorString);
                if (matcher.find()) {
                    String chatColorCode = matcher.group();
                    return ChatColor.getByChar(chatColorCode.charAt(1));
                }
            }
            return null;
        });
    }


    public CompletableFuture<List<RankPermission>> getPermissions(UUID offlinePlayer) {
        CompletableFuture<List<RankPermission>> future = new CompletableFuture<>();
        sqlGetter.getStringAsync(offlinePlayer, new DatabaseValue("PERMISSIONS")).thenAcceptAsync(s -> {
            Gson gson = new Gson();
            future.complete(gson.fromJson(s, new TypeToken<List<RankPermission>>() {
            }.getType()));
        });
        return future;
    }

    public CompletableFuture<Void> setPermissions(UUID player, List<RankPermission> rankPermissionList) {
        Gson gson = new Gson();
        return sqlGetter.setStringAsync(new DatabaseValue("PERMISSIONS", player, gson.toJson(rankPermissionList)));
    }

    public void connect() {
        sqlGetter.createTable("mafana_player_rank_manager",
                new DatabaseValue("NAME", ""),
                new DatabaseValue("RANK_ID", ""),
                new DatabaseValue("COLOR", ""),
                new DatabaseValue("PERMISSIONS", ""));
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }
}
