package me.butter.impl.game;

import me.butter.api.game.GameHandler;
import me.butter.api.game.GameState;
import me.butter.api.game.configs.GameConfig;
import me.butter.api.game.configs.ItemConfig;
import me.butter.api.game.configs.WorldConfig;
import me.butter.impl.game.configs.GameConfigImpl;
import me.butter.impl.game.configs.ItemConfigImpl;
import me.butter.impl.game.configs.WorldConfigImpl;

public class GameHandlerImpl implements GameHandler {

    private GameState gameState;
    private GameConfig gameConfig;
    private ItemConfig itemConfig;
    private WorldConfig worldConfig;

    public GameHandlerImpl() {
        gameState = GameState.LOBBY;
        gameConfig = new GameConfigImpl();
        itemConfig = new ItemConfigImpl();
        worldConfig = new WorldConfigImpl();
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public void setGameState(GameState state) {
        gameState = state;
    }

    @Override
    public GameConfig getGameConfig() {
        return gameConfig;
    }

    @Override
    public void setGameConfig(GameConfig config) {
        gameConfig = config;
    }

    @Override
    public ItemConfig getItemConfig() {
        return itemConfig;
    }

    @Override
    public void setItemConfig(ItemConfig config) {
        itemConfig = config;
    }

    @Override
    public WorldConfig getWorldConfig() {
        return worldConfig;
    }

    @Override
    public void setWorldConfig(WorldConfig config) {
        worldConfig = config;
    }
}
