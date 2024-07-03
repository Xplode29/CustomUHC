package me.butter.api.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class WorldUtils {

    public static List<Block> getCube(Location start, int width, int height, int depth, boolean filled) {
        List<Block> blocks = new ArrayList<>();

        int bx = start.getBlockX();
        int by = start.getBlockY();
        int bz = start.getBlockZ();

        for (int x = bx; x <= bx + width; x++) {
            for (int y = by; y <= by + height; y++) {
                for (int z = bz; z <= bz + depth; z++) {
                    if (filled || (
                        x == bx || x == bx + width ||
                        y == by || y == by + height ||
                        z == bz || z == bz + depth
                    )) {
                        blocks.add(new Location(start.getWorld(), x, y, z).getBlock());
                    }
                }
            }
        }

        return blocks;
    }

    public static List<Block> getSphere(Location location, int radius, boolean filled) {
        List<Block> blocks = new ArrayList<>();

        int bx = location.getBlockX();
        int by = location.getBlockY();
        int bz = location.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < radius * radius) {
                        if(filled) {
                            blocks.add(new Location(location.getWorld(), x, y, z).getBlock());
                        }
                        else if (distance >= (radius - 1) * (radius - 1)) {
                            blocks.add(new Location(location.getWorld(), x, y, z).getBlock());
                        }
                    }
                }
            }
        }

        return blocks;
    }
}
