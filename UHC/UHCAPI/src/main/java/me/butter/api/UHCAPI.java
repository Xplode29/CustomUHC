package me.butter.api;

import me.butter.api.enchant.EnchantHandler;
import me.butter.api.game.GameHandler;
import me.butter.api.item.ItemHandler;
import me.butter.api.menu.MenuHandler;
import me.butter.api.player.PlayerHandler;
import me.butter.api.scenario.ScenarioHandler;
import me.butter.api.scoreboard.ScoreboardHandler;
import me.butter.api.tab.TabHandler;
import me.butter.api.timer.TimerHandler;
import me.butter.api.world.WorldHandler;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class UHCAPI extends JavaPlugin {

    private static UHCAPI UHCAPI;

    public static void setInstance(UHCAPI UHCAPI) {
        if (UHCAPI.UHCAPI != null)
            throw new IllegalStateException("API already set!");
        UHCAPI.UHCAPI = UHCAPI;
    }

    public static UHCAPI get() {
        return UHCAPI;
    }

    public abstract PlayerHandler getPlayerHandler();

    public abstract GameHandler getGameHandler();

    public abstract EnchantHandler getEnchantHandler();

    public abstract WorldHandler getWorldHandler();

    public abstract TimerHandler getTimerHandler();

    public abstract ScoreboardHandler getScoreboardHandler();

    public abstract ScenarioHandler getScenarioHandler();

    public abstract TabHandler getTabHandler();

    public abstract ItemHandler getItemHandler();

    public abstract MenuHandler getMenuHandler();
}
