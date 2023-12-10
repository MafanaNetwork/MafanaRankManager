package me.tahacheji.mafana.rankManager;

public class RankPermission {

    private final String plugin;
    private final String permission;

    public RankPermission(String plugin, String permission) {
        this.plugin = plugin;
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public String getPlugin() {
        return plugin;
    }
}
