package me.tahacheji.mafana.rankManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.util.EncryptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RankDatabase extends MySQL {
    SQLGetter sqlGetter = new SQLGetter(this);

    public RankDatabase() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public void addRank(Rank rank) {
        UUID uuid = new EncryptionUtil().stringToUUID(rank.getRankID());
        if (!sqlGetter.exists(uuid)) {
            Gson gson = new Gson();
            sqlGetter.setString(new MysqlValue("RANK_ID", uuid, rank.getRankID()));
            sqlGetter.setString(new MysqlValue("RANK_DISPLAY_NAME", uuid, rank.getRankDisplayName()));
            sqlGetter.setInt(new MysqlValue("RANK_PRIORITY", uuid, rank.getRankPriority()));
            sqlGetter.setString(new MysqlValue("RANK_PERMISSIONS", uuid, gson.toJson(rank.getRankPermissionList())));
        }
    }

    public Rank getRank(String rankID) {
        UUID uuid = new EncryptionUtil().stringToUUID(rankID);
        if (sqlGetter.exists(uuid)) {
            Gson gson = new Gson();
            List<RankPermission> rankPermissionList = gson.fromJson(sqlGetter.getString(uuid, new MysqlValue("RANK_PERMISSIONS")), new TypeToken<List<RankPermission>>() {
            }.getType());
            Rank rank = new Rank(rankID, sqlGetter.getString(uuid, new MysqlValue("RANK_DISPLAY_NAME")),
                    sqlGetter.getInt(uuid, new MysqlValue("RANK_PRIORITY")),
                    rankPermissionList);
            return rank;
        }
        return null;
    }

    public void setRank(Rank rank) {
        UUID uuid = new EncryptionUtil().stringToUUID(rank.getRankID());
        if (sqlGetter.exists(uuid)) {
            Gson gson = new Gson();
            sqlGetter.setString(new MysqlValue("RANK_ID", uuid, rank.getRankID()));
            sqlGetter.setString(new MysqlValue("RANK_DISPLAY_NAME", uuid, rank.getRankDisplayName()));
            sqlGetter.setInt(new MysqlValue("RANK_PRIORITY", uuid, rank.getRankPriority()));
            sqlGetter.setString(new MysqlValue("RANK_PERMISSIONS", uuid, gson.toJson(rank.getRankPermissionList())));
        }
    }

    public List<Rank> getAllRanks() {
        List<Rank> allRanks = new ArrayList<>();
        try {
            List<UUID> rankUUIDs = sqlGetter.getAllUUID(new MysqlValue("RANK_ID"));
            List<String> rankDisplayNames = sqlGetter.getAllString(new MysqlValue("RANK_DISPLAY_NAME"));
            List<Integer> rankPriorities = sqlGetter.getAllIntager(new MysqlValue("RANK_PRIORITY"));
            List<String> rankPermissionsData = sqlGetter.getAllString(new MysqlValue("RANK_PERMISSIONS"));

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
        } catch (Exception e) {
            e.printStackTrace();
        }

        return allRanks;
    }

    public void removeRank(String id) {
        sqlGetter.removeString(id, new MysqlValue("RANK_ID"));
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("mafana_rank_manager",
                new MysqlValue("RANK_ID", ""),
                new MysqlValue("RANK_DISPLAY_NAME", ""),
                new MysqlValue("RANK_PRIORITY", 0),
                new MysqlValue("RANK_PERMISSIONS", ""));
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }
}
