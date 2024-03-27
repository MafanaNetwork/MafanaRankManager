package me.tahacheji.mafana.rankManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import me.tahacheji.mafana.data.DatabaseValue;
import me.tahacheji.mafana.data.MySQL;
import me.tahacheji.mafana.data.SQLGetter;
import me.tahacheji.mafana.util.EncryptionUtil;

import java.util.ArrayList;
import java.util.Collections;
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
        UUID uuid = new EncryptionUtil().stringToUUID(rank.getRankID());
        return sqlGetter.existsAsync(uuid)
                .thenComposeAsync(exists -> {
                    if (!exists) {
                        Gson gson = new Gson();
                        return sqlGetter.setStringAsync(new DatabaseValue("RANK_ID", uuid, rank.getRankID()))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("RANK_DISPLAY_NAME", uuid, rank.getRankDisplayName())))
                                .thenCompose(__ -> sqlGetter.setIntAsync(new DatabaseValue("RANK_PRIORITY", uuid, rank.getRankPriority())))
                                .thenCompose(__ -> sqlGetter.setStringAsync(new DatabaseValue("RANK_PERMISSIONS", uuid, gson.toJson(rank.getRankPermissionList()))));
                    }
                    return CompletableFuture.completedFuture(null); // No need to perform any actions
                });
    }



    public CompletableFuture<Rank> getRank(String rankID) {
        UUID uuid = new EncryptionUtil().stringToUUID(rankID);
        try {
            return sqlGetter.existsAsync(uuid)
                    .thenComposeAsync(exists -> {
                        if (exists) {
                            Gson gson = new Gson();
                            CompletableFuture<String> rankNameFuture = sqlGetter.getStringAsync(uuid, new DatabaseValue("RANK_DISPLAY_NAME"));
                            CompletableFuture<Integer> rankPriorityFuture = sqlGetter.getIntAsync(uuid, new DatabaseValue("RANK_PRIORITY"));
                            CompletableFuture<String> rankPermissionsDataFuture = sqlGetter.getStringAsync(uuid, new DatabaseValue("RANK_PERMISSIONS"));

                            // Combine all futures and wait for their completion
                            return CompletableFuture.allOf(rankNameFuture, rankPriorityFuture, rankPermissionsDataFuture)
                                    .thenApplyAsync(voidResult -> {
                                        try {
                                            // Get the results of individual futures
                                            String rankName = rankNameFuture.get();
                                            int rankPriority = rankPriorityFuture.get();
                                            String rankPermissionsData = rankPermissionsDataFuture.get();

                                            // Parse JSON and create Rank object
                                            List<RankPermission> rankPermissionList = gson.fromJson(rankPermissionsData, new TypeToken<List<RankPermission>>() {
                                            }.getType());
                                            return new Rank(rankID, rankName, rankPriority, rankPermissionList);
                                        } catch (InterruptedException | ExecutionException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                        } else {
                            // If the rank does not exist, return null
                            return CompletableFuture.completedFuture(null);
                        }
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Rank getRankSync(String rankID) {
        UUID uuid = new EncryptionUtil().stringToUUID(rankID);
        if (sqlGetter.existsSync(uuid)) {
            Gson gson = new Gson();
            List<RankPermission> rankPermissionList = gson.fromJson(sqlGetter.getStringSync(uuid, new DatabaseValue("RANK_PERMISSIONS")), new TypeToken<List<RankPermission>>() {
            }.getType());
            Rank rank = new Rank(rankID, sqlGetter.getStringSync(uuid, new DatabaseValue("RANK_DISPLAY_NAME")),
                    sqlGetter.getIntSync(uuid, new DatabaseValue("RANK_PRIORITY")),
                    rankPermissionList);
            return rank;
        }
        return null;
    }

    public List<Rank> getAllRankSync() {
        List<Rank> allRanks = new ArrayList<>();
        try {
            List<String> rankUUIDs = sqlGetter.getAllStringSync(new DatabaseValue("RANK_ID"));
            List<String> rankDisplayNames = sqlGetter.getAllStringSync(new DatabaseValue("RANK_DISPLAY_NAME"));
            List<Integer> rankPriorities = sqlGetter.getAllIntSync(new DatabaseValue("RANK_PRIORITY"));
            List<String> rankPermissionsData = sqlGetter.getAllStringSync(new DatabaseValue("RANK_PERMISSIONS"));

            for (int i = 0; i < rankUUIDs.size(); i++) {
                String id = rankUUIDs.get(i);
                String rankDisplayName = rankDisplayNames.get(i);
                int rankPriority = rankPriorities.get(i);
                String rankPermissionsDataString = rankPermissionsData.get(i);

                if (id == null || rankDisplayName == null || rankPermissionsDataString == null) {
                    continue;
                }

                Gson gson = new Gson();
                List<RankPermission> rankPermissionList = gson.fromJson(rankPermissionsDataString, new TypeToken<List<RankPermission>>() {
                }.getType());

                Rank rank = new Rank(id, rankDisplayName, rankPriority, rankPermissionList);
                allRanks.add(rank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allRanks;
    }

    public CompletableFuture<Void> setRank(Rank rank) {
        return CompletableFuture.supplyAsync(() -> {
            UUID uuid = new EncryptionUtil().stringToUUID(rank.getRankID());
            if (sqlGetter.existsSync(uuid)) {
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
            try {
                CompletableFuture<List<String>> rankUUIDsFuture = sqlGetter.getAllStringAsync(new DatabaseValue("RANK_ID"));
                CompletableFuture<List<String>> rankDisplayNamesFuture = sqlGetter.getAllStringAsync(new DatabaseValue("RANK_DISPLAY_NAME"));
                CompletableFuture<List<Integer>> rankPrioritiesFuture = sqlGetter.getAllIntegerAsync(new DatabaseValue("RANK_PRIORITY"));
                CompletableFuture<List<String>> rankPermissionsDataFuture = sqlGetter.getAllStringAsync(new DatabaseValue("RANK_PERMISSIONS"));

                return CompletableFuture.allOf(rankUUIDsFuture, rankDisplayNamesFuture, rankPrioritiesFuture, rankPermissionsDataFuture)
                        .thenApply(ignored -> {
                            List<String> rankUUIDs = rankUUIDsFuture.join();
                            List<String> rankDisplayNames = rankDisplayNamesFuture.join();
                            List<Integer> rankPriorities = rankPrioritiesFuture.join();
                            List<String> rankPermissionsData = rankPermissionsDataFuture.join();

                            List<Rank> allRanks = new ArrayList<>();
                            for (int i = 0; i < rankUUIDs.size(); i++) {
                                String id = rankUUIDs.get(i);
                                String rankDisplayName = rankDisplayNames.get(i);
                                int rankPriority = rankPriorities.get(i);
                                String rankPermissionsDataString = rankPermissionsData.get(i);

                                if (id == null || rankDisplayName == null || rankPermissionsDataString == null) {
                                    continue;
                                }

                                Gson gson = new Gson();
                                List<RankPermission> rankPermissionList = gson.fromJson(rankPermissionsDataString, new TypeToken<List<RankPermission>>() {
                                }.getType());

                                Rank rank = new Rank(id, rankDisplayName, rankPriority, rankPermissionList);
                                allRanks.add(rank);
                            }
                            return allRanks;
                        }).join();
            } catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
        });
    }


    public CompletableFuture<Void> removeRank(String id) {
        return sqlGetter.removeStringAsync(id, new DatabaseValue("RANK_ID"));
    }

    public void connect() {
        sqlGetter.createTable("mafana_rank_manager",
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
