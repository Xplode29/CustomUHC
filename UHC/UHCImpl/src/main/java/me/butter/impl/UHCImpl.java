package me.butter.impl;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.EnchantHandler;
import me.butter.api.game.GameHandler;
import me.butter.api.player.PlayerHandler;
import me.butter.api.timer.TimerHandler;
import me.butter.api.world.WorldHandler;
import me.butter.impl.commands.CommandStart;
import me.butter.impl.enchant.EnchantHandlerImpl;
import me.butter.impl.game.GameHandlerImpl;
import me.butter.impl.listeners.*;
import me.butter.impl.player.PlayerHandlerImpl;
import me.butter.impl.timer.TimerHandlerImpl;
import me.butter.impl.world.WorldHandlerImpl;
import org.bukkit.Bukkit;

public final class UHCImpl extends UHCAPI {

    private static UHCImpl instance;

    private PlayerHandler playerHandler;
    private GameHandler gameHandler;
    private EnchantHandler enchantHandler;
    private WorldHandler worldHandler;
    private TimerHandler timerHandler;

    @Override
    public void onLoad() {
        UHCAPI.setInstance(this);
        UHCImpl.instance = this;

        playerHandler = new PlayerHandlerImpl();
        gameHandler = new GameHandlerImpl();
        enchantHandler = new EnchantHandlerImpl();
        timerHandler = new TimerHandlerImpl();
    }

    @Override
    public void onEnable() {
        worldHandler = new WorldHandlerImpl();

        registerCommands();
        registerListeners();

        //Commands
        getCommand("start").setExecutor(new CommandStart());
    }

    public static UHCImpl get() {
        return instance;
    }

    void registerCommands() {
        getCommand("start").setExecutor(new CommandStart());
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
}
