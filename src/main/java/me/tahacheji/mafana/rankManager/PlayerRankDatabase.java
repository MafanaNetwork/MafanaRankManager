package me.tahacheji.mafana.rankManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.MafanaRankManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class PlayerRankDatabase extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public PlayerRankDatabase() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public void setPlayerRank(UUID offlinePlayer, String id) {
        Rank rank = MafanaRankManager.getInstance().getRankDatabase().getRank(id);
        if(rank != null) {
            sqlGetter.setString(new MysqlValue("NAME", offlinePlayer, Bukkit.getOfflinePlayer(offlinePlayer)));
            sqlGetter.setString(new MysqlValue("RANK_ID", offlinePlayer, id));
            sqlGetter.setString(new MysqlValue("PERMISSIONS", offlinePlayer, ""));
        }
    }

    public Rank getPlayerRank(UUID offlinePlayer) {
        if(sqlGetter.exists(offlinePlayer)) {
            return MafanaRankManager.getInstance().getRankDatabase().getRank(sqlGetter.getString(offlinePlayer, new MysqlValue("RANK_ID")));
        }
        return null;
    }

    public List<RankPermission> getPermissions(UUID offlinePlayer) {
        Gson gson = new Gson();
        return gson.fromJson(sqlGetter.getString(offlinePlayer, new MysqlValue("PERMISSIONS")), new TypeToken<List<RankPermission>>() {
        }.getType());
    }

    public void setPermissions(UUID player, List<RankPermission> rankPermissionList) {
        Gson gson = new Gson();
        sqlGetter.setString(new MysqlValue("PERMISSIONS", player, gson.toJson(rankPermissionList)));
    }

    @Override
    public void connect() {
        super.connect();
        if (this.isConnected()) sqlGetter.createTable("mafana_player_rank_manager",
                new MysqlValue("NAME", ""),
                new MysqlValue("RANK_ID", ""),
                new MysqlValue("PERMISSIONS", ""));
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }
}
