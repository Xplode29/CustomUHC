package me.butter.impl.world.chunkGen;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.generator.NormalChunkGenerator;
import org.bukkit.generator.BlockPopulator;

import java.util.List;

public class CustomChunkGenerator extends NormalChunkGenerator {

    private List<BlockPopulator> toAdd;

    public CustomChunkGenerator(net.minecraft.server.v1_8_R3.World world, long seed, List<BlockPopulator> toAdd) {
        super(world, seed);
        this.toAdd = toAdd;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> defaults = super.getDefaultPopulators(world);

        defaults.addAll(toAdd);

        return defaults;
    }
}
