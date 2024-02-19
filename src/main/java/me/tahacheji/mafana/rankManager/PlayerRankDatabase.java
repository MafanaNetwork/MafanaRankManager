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
        return CompletableFuture.supplyAsync(() -> {
            Rank rank = null;
            try {
                rank = MafanaRankManager.getInstance().getRankDatabase().getRank(id).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            if(rank != null) {
                sqlGetter.setStringAsync(new DatabaseValue("NAME", offlinePlayer, Bukkit.getOfflinePlayer(offlinePlayer).getName()));
                sqlGetter.setStringAsync(new DatabaseValue("RANK_ID", offlinePlayer, id));
                sqlGetter.setStringAsync(new DatabaseValue("COLOR", offlinePlayer, ""));
                sqlGetter.setStringAsync(new DatabaseValue("PERMISSIONS", offlinePlayer, ""));
            }
            return null;
        });
    }

    public CompletableFuture<Rank> getPlayerRank(UUID offlinePlayer) {
        if(sqlGetter.exists(offlinePlayer)) {
            return MafanaRankManager.getInstance().getRankDatabase().getRank(sqlGetter.getString(offlinePlayer, new DatabaseValue("RANK_ID")));
        }
        return null;
    }

    public CompletableFuture<Void> setColor(UUID uuid, String color) {
        if(sqlGetter.exists(uuid)) {
           return sqlGetter.setStringAsync(new DatabaseValue("COLOR", uuid, color));
        }
        return null;
    }

    public CompletableFuture<String> getColor(UUID uuid) {
        if(sqlGetter.exists(uuid)) {
            return sqlGetter.getStringAsync(uuid, new DatabaseValue("COLOR"));
        }
        return null;
    }

    public CompletableFuture<ChatColor> extractChatColor(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if(getColor(uuid).get() != null) {
                    Pattern pattern = Pattern.compile("(?i)(&|§)([0-9a-fk-or])");
                    Matcher matcher = pattern.matcher(getColor(uuid).get());
                    if (matcher.find()) {
                        String chatColorCode = matcher.group();
                        return ChatColor.getByChar(chatColorCode.charAt(1));
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
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

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("mafana_player_rank_manager",
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
