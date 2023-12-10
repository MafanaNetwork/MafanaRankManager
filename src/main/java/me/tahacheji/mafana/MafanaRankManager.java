package me.tahacheji.mafana;


import me.tahacheji.mafana.commandExecutor.CommandHandler;
import me.tahacheji.mafana.event.RankEvent;
import me.tahacheji.mafana.rank.*;
import me.tahacheji.mafana.rankManager.*;

import org.bukkit.plugin.java.JavaPlugin;

public final class MafanaRankManager extends JavaPlugin {

    private static MafanaRankManager instance;
    private RankDatabase rankDatabase;
    private PlayerRankDatabase playerRankDatabase;

    @Override
    public void onEnable() {
        instance = this;
        playerRankDatabase = new PlayerRankDatabase();
        rankDatabase = new RankDatabase();
        playerRankDatabase.connect();
        rankDatabase.connect();
        rankDatabase.addRank(new Copper());
        rankDatabase.addRank(new Admin());
        rankDatabase.addRank(new Gold());
        rankDatabase.addRank(new Friends());
        rankDatabase.addRank(new Staff());
        rankDatabase.addRank(new Moderator());
        rankDatabase.addRank(new Builder());
        rankDatabase.addRank(new Game_Designer());
        rankDatabase.addRank(new Game_Developer());
        rankDatabase.addRank(new Game_Manager());
        rankDatabase.addRank(new General_Manager());
        rankDatabase.addRank(new Owner());
        rankDatabase.addRank(new Diamond());
        CommandHandler.registerCommands(PlayerRankCommand.class, this);
        CommandHandler.registerCommands(RankCommand.class, this);
        getServer().getPluginManager().registerEvents(new RankEvent(), this);
    }

    @Override
    public void onDisable() {
        rankDatabase.disconnect();
        playerRankDatabase.disconnect();
    }

    public RankDatabase getRankDatabase() {
        return rankDatabase;
    }

    public PlayerRankDatabase getPlayerRankDatabase() {
        return playerRankDatabase;
    }

    public static MafanaRankManager getInstance() {
        return instance;
    }
}
