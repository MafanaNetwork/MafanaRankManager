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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RankEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");
        CompletableFuture.supplyAsync(() -> {
            try {
                Rank rank = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(player.getUniqueId()).get();
                if (rank != null) {
                    rank.registerPlayerWithRank(player);
                    rank.playerJoinEvent(player, event);
                } else {
                    MafanaRankManager.getInstance().getPlayerRankDatabase().setPlayerRank(player.getUniqueId(), "COPPER");
                    MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(player.getUniqueId()).get().registerPlayerWithRank(player);
                    MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(player.getUniqueId()).get().playerJoinEvent(player, event);
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    CompletableFuture<Rank> f = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(player.getUniqueId());
                    f.thenAcceptAsync(rank -> rank.whileOnline(player));
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(MafanaRankManager.getInstance(), 0L, 20L);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage("");
        CompletableFuture<Rank> z = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(event.getPlayer().getUniqueId());
        z.thenAcceptAsync(rank -> {
            if (rank != null) {
                rank.playerQuitEvent(event.getPlayer(), event);
            }
        });
    }

    @EventHandler
    public void talkEvent(AsyncPlayerChatEvent event) {
        CompletableFuture<Rank> z = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(event.getPlayer().getUniqueId());
        z.thenAcceptAsync(rank -> {
            if (rank != null) {
                rank.playerTalkEvent(event.getPlayer(), event);
            }
        });
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        CompletableFuture<Rank> z = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(event.getPlayer().getUniqueId());
        z.thenAcceptAsync(rank -> {
            if (rank != null) {
                rank.playerBreakBlockEvent(event.getPlayer(), event);
            }
        });
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        CompletableFuture<Rank> z = MafanaRankManager.getInstance().getPlayerRankDatabase().getPlayerRank(event.getPlayer().getUniqueId());
        z.thenAcceptAsync(rank -> {
            if (rank != null) {
                rank.playerPlaceBlockEvent(event.getPlayer(), event);
            }
        });
    }


}
