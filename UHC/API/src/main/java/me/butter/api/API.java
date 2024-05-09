package me.butter.api;

import me.butter.api.player.PlayerHandler;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class API extends JavaPlugin {

    private static API api;

    public static void setInstance(API api) {
        if (API.api != null)
            throw new IllegalStateException("API already set!");
        API.api = api;
    }

    public static API get() {
        return api;
    }

    public abstract PlayerHandler getPlayerHandler();
}
