package me.butter.api.structures;

import org.bukkit.World;

import java.util.List;

public interface StructureHandler {

    List<Structure> getStructures();
    Structure getStructure(Class<? extends Structure> structClass, World world, double x, double y, double z);
    void addStructure(Structure structure);

    void loadStructures();

    void spawnStructure(Structure structure);
    void clearStructure(Class<? extends Structure> structClass, World world, double x, double y, double z);
}
