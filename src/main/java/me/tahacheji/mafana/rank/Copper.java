package me.tahacheji.mafana.rank;

import me.tahacheji.mafana.rankManager.Rank;
import me.tahacheji.mafana.rankManager.RankPermission;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;


public class Copper extends Rank {
    public Copper() {
        super("COPPER",
                ChatColor.DARK_GRAY + "[" + ChatColor.of(new Color(184, 115, 51)) + "Copper" + ChatColor.DARK_GRAY + "]"
                ,1, new RankPermission("MafanaRankManager", "mafana.copper"));
    }
}
