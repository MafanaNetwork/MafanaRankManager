package me.tahacheji.mafana.rankManager;

import org.bukkit.OfflinePlayer;

public class PlayerRank {

    private OfflinePlayer player;
    private String rankId;

    public PlayerRank(OfflinePlayer player, String rankId) {
        this.player = player;
        this.rankId = rankId;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public String getRankId() {
        return rankId;
    }

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    public void setRankId(String rankId) {
        this.rankId = rankId;
    }
}
