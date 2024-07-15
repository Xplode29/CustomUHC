package me.butter.impl;

import me.butter.api.UHCAPI;
import me.butter.api.clickablechat.ClickableChatHandler;
import me.butter.api.customEntities.CustomEntitiesHandler;
import me.butter.api.enchant.EnchantHandler;
import me.butter.api.game.GameHandler;
import me.butter.api.game.GameState;
import me.butter.api.item.ItemHandler;
import me.butter.api.menu.MenuHandler;
import me.butter.api.module.ModuleHandler;
import me.butter.api.nametagColor.NametagColorHandler;
import me.butter.api.player.PlayerHandler;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.potion.PotionEffectHandler;
import me.butter.api.scenario.ScenarioHandler;
import me.butter.api.scoreboard.ScoreboardHandler;
import me.butter.api.structures.StructureHandler;
import me.butter.api.tab.TabHandler;
import me.butter.api.timer.TimerHandler;
import me.butter.api.world.WorldHandler;
import me.butter.impl.clickablechat.ClickableChatHandlerImpl;
import me.butter.impl.commands.CommandsRemover;
import me.butter.impl.commands.debug.DebugCommands;
import me.butter.impl.commands.host.HostCommands;
import me.butter.impl.customEntities.CustomEntitiesHandlerImpl;
import me.butter.impl.enchant.EnchantHandlerImpl;
import me.butter.impl.game.GameHandlerImpl;
import me.butter.impl.item.ItemHandlerImpl;
import me.butter.impl.listeners.*;
import me.butter.impl.menu.MenuHandlerImpl;
import me.butter.impl.module.ModuleHandlerImpl;
import me.butter.impl.nametagColor.NametagColorHandlerImpl;
import me.butter.impl.player.PlayerHandlerImpl;
import me.butter.impl.player.PotionUpdaterTask;
import me.butter.impl.potion.PotionEffectHandlerImpl;
import me.butter.impl.scenario.ScenarioHandlerImpl;
import me.butter.impl.scoreboard.ScoreboardHandlerImpl;
import me.butter.impl.scoreboard.list.LobbyScoreboard;
import me.butter.impl.structures.StructureHandlerImpl;
import me.butter.impl.tab.TabHandlerImpl;
import me.butter.impl.tab.list.MainTab;
import me.butter.impl.timer.TimerHandlerImpl;
import me.butter.impl.world.WorldHandlerImpl;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public final class UHCBase extends UHCAPI {

    private static UHCBase instance;

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
    private ModuleHandler moduleHandler;
    private StructureHandler structureHandler;
    private ClickableChatHandler clickableChatHandler;
    private PotionEffectHandler potionEffectHandler;
    private CustomEntitiesHandler customEntitiesHandler;
    private NametagColorHandler nametagColorHandler;

    @Override
    public void onLoad() {
        UHCAPI.setInstance(this);
        UHCBase.instance = this;
    }

    @Override
    public void onEnable() {
        playerHandler = new PlayerHandlerImpl();
        moduleHandler = new ModuleHandlerImpl();
        gameHandler = new GameHandlerImpl();

        tabHandler = new TabHandlerImpl();
        scoreboardHandler = new ScoreboardHandlerImpl();

        enchantHandler = new EnchantHandlerImpl();
        timerHandler = new TimerHandlerImpl();
        scenarioHandler = new ScenarioHandlerImpl();
        worldHandler = new WorldHandlerImpl();
        itemHandler = new ItemHandlerImpl();
        menuHandler = new MenuHandlerImpl();
        structureHandler = new StructureHandlerImpl();
        clickableChatHandler = new ClickableChatHandlerImpl();
        potionEffectHandler = new PotionEffectHandlerImpl();
        customEntitiesHandler = new CustomEntitiesHandlerImpl();
        nametagColorHandler = new NametagColorHandlerImpl();

        registerListeners();
        registerCommands();

        for(Player player : Bukkit.getOnlinePlayers()) {
            playerHandler.addPlayer(player);

            UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
            uhcPlayer.setName(player.getName());

            uhcPlayer.resetPlayer();

            uhcPlayer.setPlayerState(PlayerState.IN_LOBBY);
            player.setGameMode(GameMode.SURVIVAL);

            Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.teleport(JoinEvents.spawnLocation), 2);

            UHCAPI.getInstance().getItemHandler().giveLobbyItems(uhcPlayer);

            UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(LobbyScoreboard.class, uhcPlayer);
            UHCAPI.getInstance().getTabHandler().setPlayerTab(MainTab.class, uhcPlayer);

            for(Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if(onlinePlayer != player) {
                    player.showPlayer(onlinePlayer);
                }
            }
        }

        new PotionUpdaterTask();

        Bukkit.getScheduler().runTaskLater(this, CommandsRemover::clearBukkitCommands, 5 * 20);
    }

    @Override
    public void reset() {
        if(gameHandler.getGameState() == GameState.LOBBY) {
            gameHandler.getGameConfig().setStarting(false);
        }
        else {
            gameHandler.setGameState(GameState.LOBBY);
            Bukkit.getServer().reload();
        }
    }

    public static UHCBase getInstance() {
        return instance;
    }

    void registerCommands() {
        getCommand("host").setExecutor(new HostCommands());
        getCommand("debug").setExecutor(new DebugCommands());
    }

    void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new BlockEvents(), this);
        Bukkit.getPluginManager().registerEvents(new DamageHealthEvents(), this);
        Bukkit.getPluginManager().registerEvents(new InventoriesEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ItemEvents(), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvents(), this);
        Bukkit.getPluginManager().registerEvents(new OtherEvents(), this);
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

    @Override
    public ModuleHandler getModuleHandler() {
        return moduleHandler;
    }

    @Override
    public StructureHandler getStructureHandler() {
        return structureHandler;
    }

    @Override
    public ClickableChatHandler getClickableChatHandler() {
        return clickableChatHandler;
    }

    @Override
    public PotionEffectHandler getPotionEffectHandler() {
        return potionEffectHandler;
    }

    @Override
    public CustomEntitiesHandler getCustomEntitiesHandler() {
        return customEntitiesHandler;
    }

    @Override
    public NametagColorHandler getNametagColorHandler() {
        return nametagColorHandler;
    }
}
