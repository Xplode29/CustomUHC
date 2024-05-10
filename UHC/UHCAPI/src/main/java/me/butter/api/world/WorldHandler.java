package me.butter.api.world;

import org.bukkit.World;

public interface WorldHandler {

    World getWorld(); void setWorld(World world);

    String getWorldName();

    void createWorld(String worldName);

    void deleteWorld(String worldName);

    void loadWorld(String worldName);

    OrePopulator getOrePopulator();
}
