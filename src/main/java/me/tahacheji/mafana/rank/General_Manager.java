package me.tahacheji.mafana.rank;

import me.tahacheji.mafana.rankManager.Rank;
import me.tahacheji.mafana.rankManager.RankPermission;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;


public class General_Manager extends Rank {
    public General_Manager() {
        super("GENERAL_MANAGER",
                ChatColor.DARK_GRAY + "[" + ChatColor.of(new Color(255, 229, 180)) + "General-Manager" + ChatColor.DARK_GRAY + "]"
                ,12, new RankPermission("MafanaRankManager", "mafana.general.manager"), new RankPermission("MafanaRankManager", "mafana.game.manager"), new RankPermission("MafanaRankManager", "mafana.game.developer"), new RankPermission("MafanaRankManager", "mafana.game.designer"), new RankPermission("WorldEdit", "worldedit.*"), new RankPermission("MafanaRankManager", "mafana.builder"), new RankPermission("MafanaRankManager", "mafana.moderator"), new RankPermission("MafanaRankManager", "mafana.staff"), new RankPermission("MafanaRankManager", "mafana.friend"), new RankPermission("MafanaRankManager", "mafana.diamond") , new RankPermission("MafanaRankManager", "mafana.gold"), new RankPermission("MafanaRankManager", "mafana.silver"),
                new RankPermission("MafanaRankManager", "mafana.copper"));
    }
}
