package me.butter.api.game;

import me.butter.api.game.configs.GameConfig;
import me.butter.api.game.configs.InventoryConfig;
import me.butter.api.game.configs.ItemConfig;
import me.butter.api.game.configs.WorldConfig;

public interface GameHandler {

    GameState getGameState(); void setGameState(GameState state);

    GameConfig getGameConfig(); void setGameConfig(GameConfig config);

    InventoryConfig getInventoriesConfig(); void setInventoriesConfig(InventoryConfig config);

    ItemConfig getItemConfig(); void setItemConfig(ItemConfig config);

    WorldConfig getWorldConfig(); void setWorldConfig(WorldConfig config);
}
