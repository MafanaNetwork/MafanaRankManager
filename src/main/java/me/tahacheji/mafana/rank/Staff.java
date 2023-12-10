package me.tahacheji.mafana.rank;

import me.tahacheji.mafana.rankManager.Rank;
import me.tahacheji.mafana.rankManager.RankPermission;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;


public class Staff extends Rank {
    public Staff() {
        super("STAFF",
                ChatColor.DARK_GRAY + "[" + ChatColor.of(new Color(156,166,255)) + "Staff" + ChatColor.DARK_GRAY + "]"
                ,6, new RankPermission("MafanaRankManager", "mafana.staff"), new RankPermission("MafanaRankManager", "mafana.friend"), new RankPermission("MafanaRankManager", "mafana.diamond") , new RankPermission("MafanaRankManager", "mafana.gold"), new RankPermission("MafanaRankManager", "mafana.silver"),
                new RankPermission("MafanaRankManager", "mafana.copper"));
    }
}
