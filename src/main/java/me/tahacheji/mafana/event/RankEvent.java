package me.tahacheji.mafana.event;

import me.tahacheji.mafana.MafanaRankManager;
import me.tahacheji.mafana.rankManager.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class RankEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");
        Rank rank = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(player);
        if(rank != null) {
            rank.registerPlayerWithRank(player);
            rank.playerJoinEvent(player, event);
        } else {
            MafanaRankManager.getInstance().getPlayerRankDatabase().setPlayerRank(player, "COPPER");
            MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(player).registerPlayerWithRank(player);
            MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(player).playerJoinEvent(player, event);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if(player.isOnline()) {
                    MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(player).whileOnline(player);
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimer(MafanaRankManager.getInstance(), 0L, 20L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Rank rank = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(event.getPlayer());
        if(rank != null) {
            rank.playerQuitEvent(event.getPlayer(), event);
        }
    }

    @EventHandler
    public void talkEvent(AsyncPlayerChatEvent event) {
        Rank rank = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(event.getPlayer());
        if(rank != null) {
            rank.playerTalkEvent(event.getPlayer(), event);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Rank rank = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(event.getPlayer());
        if(rank != null) {
            rank.playerBreakBlockEvent(event.getPlayer(), event);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Rank rank = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(event.getPlayer());
        if(rank != null) {
            rank.playerPlaceBlockEvent(event.getPlayer(), event);
        }
    }


}
