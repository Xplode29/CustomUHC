package me.butter.impl;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.EnchantHandler;
import me.butter.api.game.GameHandler;
import me.butter.api.item.ItemHandler;
import me.butter.api.menu.MenuHandler;
import me.butter.api.player.PlayerHandler;
import me.butter.api.scenario.ScenarioHandler;
import me.butter.api.scoreboard.ScoreboardHandler;
import me.butter.api.tab.TabHandler;
import me.butter.api.timer.TimerHandler;
import me.butter.api.utils.ChatUtils;
import me.butter.api.utils.SkullsGenerator;
import me.butter.api.world.WorldHandler;
import me.butter.impl.commands.CommandHost;
import me.butter.impl.commands.CommandRules;
import me.butter.impl.enchant.EnchantHandlerImpl;
import me.butter.impl.game.GameHandlerImpl;
import me.butter.impl.item.ItemHandlerImpl;
import me.butter.impl.listeners.*;
import me.butter.impl.menu.MenuHandlerImpl;
import me.butter.impl.player.PlayerHandlerImpl;
import me.butter.impl.scenario.ScenarioHandlerImpl;
import me.butter.impl.scoreboard.ScoreboardHandlerImpl;
import me.butter.impl.tab.TabHandlerImpl;
import me.butter.impl.timer.TimerHandlerImpl;
import me.butter.impl.world.WorldHandlerImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public final class UHCImpl extends UHCAPI {

    private static UHCImpl instance;

    private PlayerHandler playerHandler;
    private GameHandler gameHandler;
    private EnchantHandler enchantHandler;
    private WorldHandler worldHandler;
    private TimerHandler timerHandler;
    private ScoreboardHandler scoreboardHandler;
    private ScenarioHandler scenarioHandler;
    private TabHandler tabHandler;
    private ItemHandler itemHandler;
    private MenuHandler menuHandler;

    @Override
    public void onLoad() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer("Please Reconnect!");
        }

        UHCAPI.setInstance(this);
        UHCImpl.instance = this;

        playerHandler = new PlayerHandlerImpl();
        gameHandler = new GameHandlerImpl();
        enchantHandler = new EnchantHandlerImpl();
        timerHandler = new TimerHandlerImpl();
        scenarioHandler = new ScenarioHandlerImpl();
    }

    @Override
    public void onEnable() {
        worldHandler = new WorldHandlerImpl();
        scoreboardHandler = new ScoreboardHandlerImpl();
        tabHandler = new TabHandlerImpl();
        itemHandler = new ItemHandlerImpl();
        menuHandler = new MenuHandlerImpl();

        registerCommands();
        registerListeners();
        Bukkit.getPluginManager().registerEvents(new testListener(), this);

        Bukkit.broadcastMessage(ChatUtils.SEPARATOR.getPrefix() + ChatUtils.SEPARATOR.getPrefix() + ChatUtils.SEPARATOR.getPrefix());
    }

    public static UHCImpl get() {
        return instance;
    }

    void registerCommands() {
        getCommand("host").setExecutor(new CommandHost());
        getCommand("rules").setExecutor(new CommandRules());
    }

    void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new UHCPlayerListener(), this);

        Bukkit.getPluginManager().registerEvents(new LobbyListener(), this);
        Bukkit.getPluginManager().registerEvents(new TeleportListener(), this);
        Bukkit.getPluginManager().registerEvents(new StartingListener(), this);
        Bukkit.getPluginManager().registerEvents(new GameListener(), this);

        Bukkit.getPluginManager().registerEvents(new CombatListener(), this);
    }

    @Override
    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    @Override
    public GameHandler getGameHandler() {
        return gameHandler;
    }

    @Override
    public EnchantHandler getEnchantHandler() {
        return enchantHandler;
    }

    @Override
    public WorldHandler getWorldHandler() {
        return worldHandler;
    }

    @Override
    public TimerHandler getTimerHandler() {
        return timerHandler;
    }

    @Override
    public ScoreboardHandler getScoreboardHandler() {
        return scoreboardHandler;
    }

    @Override
    public ScenarioHandler getScenarioHandler() {
        return scenarioHandler;
    }

    @Override
    public TabHandler getTabHandler() {
        return tabHandler;
    }

    @Override
    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public MenuHandler getMenuHandler() {
        return menuHandler;
    }
}
