package me.butter.impl.world.pregen;

import me.butter.api.UHCAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class PregenTask extends BukkitRunnable {

    public static boolean finished;

    private final int startX, startZ;
    private int x, z;
    private double currentChunkLoad;
    private final double totalChunkToLoad;

    private final World world;

    public PregenTask(World world, int radius) {
        finished = false;
        this.totalChunkToLoad = Math.pow(radius, 2.0D) / 64.0D;
        this.currentChunkLoad = 0.0D;
        this.world = world;
        radius += 150;

        this.startX = -radius; this.startZ = -radius;
        this.x = this.startX; this.z = this.startZ;
    }

    @Override
    public void run() {
        for (int i = 0; i < 30 && !finished; i++) {
            Location loc = new Location(world, x, 0, z);
            if (!loc.getChunk().isLoaded())
                loc.getWorld().loadChunk(loc.getChunk().getX(), loc.getChunk().getZ(), true);
            x += 16;
            currentChunkLoad ++;
            if (x > -startX) {
                x = startX;
                z += 16;
                if (z > -startZ) {
                    finished = true;
                    currentChunkLoad = totalChunkToLoad;
                    cancel();
                }
            }
        }

        UHCAPI.get().getPlayerHandler().getPlayers().forEach(p -> p.sendActionBar(
                getProgressBar(currentChunkLoad, totalChunkToLoad, 15, '|', ChatColor.GREEN, ChatColor.RED)
        ));
    }

    public static String getProgressBar(double current, double max, int totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        int progressBars = (int) (totalBars * (current / max));

        return com.google.common.base.Strings.repeat("" + completedColor + symbol, progressBars)
                + com.google.common.base.Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
}
