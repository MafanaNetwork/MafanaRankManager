package me.tahacheji.mafana.rank;

import me.tahacheji.mafana.rankManager.Rank;
import me.tahacheji.mafana.rankManager.RankPermission;
import net.md_5.bungee.api.ChatColor;

import java.awt.*;


public class Game_Manager extends Rank {
    public Game_Manager() {
        super("GAME_MANAGER",
                ChatColor.DARK_GRAY + "[" + ChatColor.of(new Color(233, 220, 201)) + "Game-Manager" + ChatColor.DARK_GRAY + "]"
                ,11, new RankPermission("MafanaRankManager", "mafana.game.manager"), new RankPermission("MafanaRankManager", "mafana.game.developer"), new RankPermission("MafanaRankManager", "mafana.game.designer"), new RankPermission("WorldEdit", "worldedit.*"), new RankPermission("MafanaRankManager", "mafana.builder"), new RankPermission("MafanaRankManager", "mafana.moderator"), new RankPermission("MafanaRankManager", "mafana.staff"), new RankPermission("MafanaRankManager", "mafana.friend"), new RankPermission("MafanaRankManager", "mafana.diamond") , new RankPermission("MafanaRankManager", "mafana.gold"), new RankPermission("MafanaRankManager", "mafana.silver"),
                new RankPermission("MafanaRankManager", "mafana.copper"));
    }
}
