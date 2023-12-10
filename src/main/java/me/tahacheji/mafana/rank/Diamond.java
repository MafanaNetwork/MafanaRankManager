package me.tahacheji.mafana.rank;

import me.tahacheji.mafana.rankManager.Rank;
import me.tahacheji.mafana.rankManager.RankPermission;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;


public class Diamond extends Rank {
    public Diamond() {
        super("DIAMOND",
                ChatColor.DARK_GRAY + "[" + ChatColor.of(new Color(217,235,244)) + "Diamond" + ChatColor.DARK_GRAY + "]"
                ,4, new RankPermission("MafanaRankManager", "mafana.diamond") , new RankPermission("MafanaRankManager", "mafana.gold"), new RankPermission("MafanaRankManager", "mafana.silver"),
                new RankPermission("MafanaRankManager", "mafana.copper"));
    }
}
