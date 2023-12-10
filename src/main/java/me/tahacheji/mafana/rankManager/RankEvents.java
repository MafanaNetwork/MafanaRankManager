package me.tahacheji.mafana.rankManager;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public interface RankEvents {

    default boolean playerJoinEvent(Player player, PlayerJoinEvent event) {return false;}
    default boolean playerQuitEvent(Player player, PlayerQuitEvent event) {return false;}
    default boolean playerTalkEvent(Player player, AsyncPlayerChatEvent event) {return false;}
    default boolean playerBreakBlockEvent(Player player, BlockBreakEvent event) {return false;}
    default boolean playerPlaceBlockEvent(Player player, BlockPlaceEvent event) {return false;}
    default boolean whileOnline(Player player) {return false;}

}
