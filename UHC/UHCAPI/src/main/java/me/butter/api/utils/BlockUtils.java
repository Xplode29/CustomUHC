package me.butter.api.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class BlockUtils {
    public static void fillBlocks(World world, int xStart, int yStart, int zStart, int widthX, int height, int widthZ, Material mat) {
        for(int x = xStart; x < xStart + widthX; x++) {
            for(int y = yStart; y < yStart + height; y++) {
                for(int z = zStart; z < zStart + widthZ; z++) {
                    Location loc = new Location(world, x, y, z);
                    loc.getBlock().setType(mat);
                }
            }
        }
    }
}
