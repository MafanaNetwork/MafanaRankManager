package me.tahacheji.mafana.rankManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.TahaCheji.mysqlData.MySQL;
import me.TahaCheji.mysqlData.MysqlValue;
import me.TahaCheji.mysqlData.SQLGetter;
import me.tahacheji.mafana.MafanaRankManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerRankDatabase extends MySQL {

    SQLGetter sqlGetter = new SQLGetter(this);

    public PlayerRankDatabase() {
        super("162.254.145.231", "3306", "51252", "51252", "346a1ef0fc");
    }

    public void setPlayerRank(UUID offlinePlayer, String id) {
        Rank rank = MafanaRankManager.getInstance().getRankDatabase().getRank(id);
        if(rank != null) {
            sqlGetter.setString(new MysqlValue("NAME", offlinePlayer, Bukkit.getOfflinePlayer(offlinePlayer).getName()));
            sqlGetter.setString(new MysqlValue("RANK_ID", offlinePlayer, id));
            sqlGetter.setString(new MysqlValue("COLOR", offlinePlayer, ""));
            sqlGetter.setString(new MysqlValue("PERMISSIONS", offlinePlayer, ""));
        }
    }

    public Rank getPlayerRank(UUID offlinePlayer) {
        if(sqlGetter.exists(offlinePlayer)) {
            return MafanaRankManager.getInstance().getRankDatabase().getRank(sqlGetter.getString(offlinePlayer, new MysqlValue("RANK_ID")));
        }
        return null;
    }

    public void setColor(UUID uuid, String color) {
        if(sqlGetter.exists(uuid)) {
            sqlGetter.setString(new MysqlValue("COLOR", uuid, color));
        }
    }

    public String getColor(UUID uuid) {
        if(sqlGetter.exists(uuid)) {
            return sqlGetter.getString(uuid, new MysqlValue("COLOR"));
        }
        return null;
    }

    public ChatColor extractChatColor(UUID uuid) {
        if(getColor(uuid) != null) {
            Pattern pattern = Pattern.compile("(?i)(&|§)([0-9a-fk-or])");
            Matcher matcher = pattern.matcher(getColor(uuid));
            if (matcher.find()) {
                String chatColorCode = matcher.group();
                return ChatColor.getByChar(chatColorCode.charAt(1));
            }
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
                new MysqlValue("COLOR", ""),
                new MysqlValue("PERMISSIONS", ""));
    }

    @Override
    public SQLGetter getSqlGetter() {
        return sqlGetter;
    }
}
