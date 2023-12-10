package me.tahacheji.mafana.rankManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.MafanaRankManager;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class PlayerRankDatabase extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public PlayerRankDatabase() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public void setPlayerRank(OfflinePlayer offlinePlayer, String id) {
        Rank rank = MafanaRankManager.getInstance().getRankDatabase().getRank(id);
        if(rank != null) {
            UUID uuid = offlinePlayer.getUniqueId();
            sqlGetter.setString(new MysqlValue("NAME", uuid, offlinePlayer.getName()));
            sqlGetter.setString(new MysqlValue("RANK_ID", uuid, id));
            sqlGetter.setString(new MysqlValue("PERMISSIONS", uuid, ""));
        }
    }

    public Rank getPlayerRank(OfflinePlayer offlinePlayer) {
        if(sqlGetter.exists(offlinePlayer.getUniqueId())) {
            return MafanaRankManager.getInstance().getRankDatabase().getRank(sqlGetter.getString(offlinePlayer.getUniqueId(), new MysqlValue("RANK_ID")));
        }
        return null;
    }

    public List<RankPermission> getPermissions(OfflinePlayer offlinePlayer) {
        Gson gson = new Gson();
        return gson.fromJson(sqlGetter.getString(offlinePlayer.getUniqueId(), new MysqlValue("PERMISSIONS")), new TypeToken<List<RankPermission>>() {
        }.getType());
    }

    public void setPermissions(OfflinePlayer player, List<RankPermission> rankPermissionList) {
        Gson gson = new Gson();
        sqlGetter.setString(new MysqlValue("PERMISSIONS", player.getUniqueId(), gson.toJson(rankPermissionList)));
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
