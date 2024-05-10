package me.butter.impl.world;

import net.minecraft.server.v1_8_R3.BiomeBase;

import java.lang.reflect.Field;
import java.util.Random;

public class BiomeSwapper {

    public static void init() {
        try {
            Field biomeField = BiomeBase.class.getDeclaredField("biomes");
            biomeField.setAccessible(true);
            if (biomeField.get(null) instanceof BiomeBase[]) {
                BiomeBase[] biomes = (BiomeBase[])biomeField.get(null);
                Random rand = new Random();
                for (BiomeBase biome : biomes)
                    if (biome != null && biome != BiomeBase.ROOFED_FOREST && biome != BiomeBase.BIRCH_FOREST && biome != BiomeBase.TAIGA && biome != BiomeBase.PLAINS && biome != BiomeBase.FOREST && biome != BiomeBase.MEGA_TAIGA_HILLS && biome != BiomeBase.TAIGA_HILLS && biome != BiomeBase.MEGA_TAIGA) {
                        if (rand.nextInt(5) == 0) swap(biomes, biome, BiomeBase.FOREST);
                        else swap(biomes, biome, BiomeBase.PLAINS);
                    }

            }
            biomeField.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void swap(BiomeBase[] biomes, BiomeBase from, BiomeBase to) {
        swap(biomes, from.id, to);
    }

    private static void swap(BiomeBase[] biomes, int from, BiomeBase to) {
        biomes[from] = to;
    }
}