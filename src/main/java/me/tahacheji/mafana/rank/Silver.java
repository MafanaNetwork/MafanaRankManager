package me.tahacheji.mafana.rank;

import me.tahacheji.mafana.rankManager.Rank;
import me.tahacheji.mafana.rankManager.RankPermission;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;


public class Silver extends Rank {
    public Silver() {
        super("SILVER",
                ChatColor.DARK_GRAY + "[" + ChatColor.of(new Color(192, 192, 192)) + "Silver" + ChatColor.DARK_GRAY + "]"
                ,2, new RankPermission("MafanaRankManager", "mafana.silver"),
                new RankPermission("MafanaRankManager", "mafana.copper"));
    }
}
