package me.tahacheji.mafana.rankManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.tahacheji.mafana.data.DatabaseValue;
import me.tahacheji.mafana.data.MySQL;
import me.tahacheji.mafana.data.SQLGetter;
import me.tahacheji.mafana.util.EncryptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RankDatabase extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);

    public RankDatabase() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public CompletableFuture<Void> addRank(Rank rank) {
        return CompletableFuture.supplyAsync(() -> {
            UUID uuid = new EncryptionUtil().stringToUUID(rank.getRankID());
            if (!sqlGetter.exists(uuid)) {
                Gson gson = new Gson();
                sqlGetter.setStringAsync(new DatabaseValue("RANK_ID", uuid, rank.getRankID()));
                sqlGetter.setStringAsync(new DatabaseValue("RANK_DISPLAY_NAME", uuid, rank.getRankDisplayName()));
                sqlGetter.setIntAsync(new DatabaseValue("RANK_PRIORITY", uuid, rank.getRankPriority()));
                sqlGetter.setStringAsync(new DatabaseValue("RANK_PERMISSIONS", uuid, gson.toJson(rank.getRankPermissionList())));
            }
            return null;
        });
    }

    public CompletableFuture<Rank> getRank(String rankID) {
        return CompletableFuture.supplyAsync(() -> {
            UUID uuid = new EncryptionUtil().stringToUUID(rankID);
            if (sqlGetter.exists(uuid)) {
                try {
                    Gson gson = new Gson();
                    List<RankPermission> rankPermissionList = gson.fromJson(sqlGetter.getStringAsync(uuid, new DatabaseValue("RANK_PERMISSIONS")).get(), new TypeToken<List<RankPermission>>() {
                        }.getType());
                    return new Rank(rankID, sqlGetter.getStringAsync(uuid, new DatabaseValue("RANK_DISPLAY_NAME")).get(),
                            sqlGetter.getIntAsync(uuid, new DatabaseValue("RANK_PRIORITY")).get(),
                            rankPermissionList);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        });
    }

    public CompletableFuture<Void> setRank(Rank rank) {
        return CompletableFuture.supplyAsync(() -> {
            UUID uuid = new EncryptionUtil().stringToUUID(rank.getRankID());
            if (sqlGetter.exists(uuid)) {
                Gson gson = new Gson();
                sqlGetter.setStringAsync(new DatabaseValue("RANK_ID", uuid, rank.getRankID()));
                sqlGetter.setStringAsync(new DatabaseValue("RANK_DISPLAY_NAME", uuid, rank.getRankDisplayName()));
                sqlGetter.setIntAsync(new DatabaseValue("RANK_PRIORITY", uuid, rank.getRankPriority()));
                sqlGetter.setStringAsync(new DatabaseValue("RANK_PERMISSIONS", uuid, gson.toJson(rank.getRankPermissionList())));
            }
            return null;
        });
    }

    public CompletableFuture<List<Rank>> getAllRanks() {
        return CompletableFuture.supplyAsync(() -> {
            List<Rank> allRanks = new ArrayList<>();
            try {
                List<UUID> rankUUIDs = sqlGetter.getAllUUIDAsync(new DatabaseValue("RANK_ID")).get();
                List<String> rankDisplayNames = sqlGetter.getAllStringAsync(new DatabaseValue("RANK_DISPLAY_NAME")).get();
                List<Integer> rankPriorities = sqlGetter.getAllIntegerAsync(new DatabaseValue("RANK_PRIORITY")).get();
                List<String> rankPermissionsData = sqlGetter.getAllStringAsync(new DatabaseValue("RANK_PERMISSIONS")).get();

                for (int i = 0; i < rankUUIDs.size(); i++) {
                    UUID uuid = rankUUIDs.get(i);
                    String rankDisplayName = rankDisplayNames.get(i);
                    int rankPriority = rankPriorities.get(i);
                    String rankPermissionsDataString = rankPermissionsData.get(i);

                    if (uuid == null || rankDisplayName == null || rankPermissionsDataString == null) {
                        continue;
                    }

                    Gson gson = new Gson();
                    List<RankPermission> rankPermissionList = gson.fromJson(rankPermissionsDataString, new TypeToken<List<RankPermission>>() {
                    }.getType());

                    Rank rank = new Rank(uuid.toString(), rankDisplayName, rankPriority, rankPermissionList);
                    allRanks.add(rank);
                }
                return allRanks;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public CompletableFuture<Void> removeRank(String id) {
        return sqlGetter.removeStringAsync(id, new DatabaseValue("RANK_ID"));
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("mafana_rank_manager",
                new DatabaseValue("RANK_ID", ""),
                new DatabaseValue("RANK_DISPLAY_NAME", ""),
                new DatabaseValue("RANK_PRIORITY", 0),
                new DatabaseValue("RANK_PERMISSIONS", ""));
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }
}
