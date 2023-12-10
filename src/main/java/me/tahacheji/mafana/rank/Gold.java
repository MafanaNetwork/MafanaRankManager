package me.tahacheji.mafana.rank;

import me.tahacheji.mafana.rankManager.Rank;
import me.tahacheji.mafana.rankManager.RankPermission;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;


public class Gold extends Rank {
    public Gold() {
        super("GOLD",
                ChatColor.DARK_GRAY + "[" + ChatColor.of(new Color(170,136,34)) + "Gold" + ChatColor.DARK_GRAY + "]"
                ,3, new RankPermission("MafanaRankManager", "mafana.gold"), new RankPermission("MafanaRankManager", "mafana.silver"),
                new RankPermission("MafanaRankManager", "mafana.copper"));
    }

}
