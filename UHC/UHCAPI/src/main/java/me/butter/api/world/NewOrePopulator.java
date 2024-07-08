package me.butter.api.world;

import org.bukkit.Material;
import org.bukkit.World;

import java.util.Random;

public class NewOrePopulator extends OrePopulator {

    @Override
    public void generateVein(final World world, final Random rand, final int startX, final int startY, final int startZ, final int size, final OrePopulator.Rule rule) {
        int x = startX, y = startY, z = startZ;
        boolean doesContinue;

        if (world.getBlockAt(x, y, z).getType() == Material.STONE) {
            doesContinue = true;
            for(int j = 0; j <= rule.size; j++) {
                if (doesContinue) {

                    world.getBlockAt(x, y, z).setType(rule.id);

                    switch (rand.nextInt(6)) {  // The direction chooser
                        case 0: x++; break;
                        case 1: y++; break;
                        case 2: z++; break;
                        case 3: x--; break;
                        case 4: y--; break;
                        case 5: z--; break;
                    }
                    doesContinue = world.getBlockAt(x, y, z).getType() == Material.STONE || world.getBlockAt(x, y, z).getType() != rule.id;
                }
            }
        }
    }
}
