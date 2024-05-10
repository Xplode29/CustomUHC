package me.butter.impl.world.pregen;

import me.butter.api.UHCAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;

public class PregenTask extends BukkitRunnable {

    public static boolean finished;

    private Double currentChunkLoad;

    private final Double totalChunkToLoad;

    private int x, z;

    private final int radius;

    private final World world;

    public PregenTask(World world, int radius) {
        radius += 150;
        finished = false;
        this.totalChunkToLoad = Math.pow(radius, 2.0D) / 64.0D;
        this.currentChunkLoad = 0.0D;
        this.x = -radius;
        this.z = -radius;
        this.world = world;
        this.radius = radius;
    }

    @Override
    public void run() {
        for (int i = 0; i < 40 && !finished; i++) {
            Location loc = new Location(world, x, 0.0D, z);
            if (!loc.getChunk().isLoaded())
                loc.getWorld().loadChunk(loc.getChunk().getX(), loc.getChunk().getZ(), true);
            x = x + 16;
            currentChunkLoad = currentChunkLoad + 1.0D;
            if (x > radius) {
                x = -radius;
                z = z + 16;
                if (z > radius) {
                    finished = true;
                    currentChunkLoad = totalChunkToLoad;
                    cancel();
                }
            }
        }

        double percentage = currentChunkLoad / totalChunkToLoad * 100.0D;

        if (percentage > 99.9)
            return;
        DecimalFormat format = new DecimalFormat("##.#");
        String pb = getProgressBar(currentChunkLoad.intValue(), totalChunkToLoad.intValue(), 15, '|', ChatColor.GREEN, ChatColor.RED);
        UHCAPI.get().getPlayerHandler().getPlayers().forEach(p -> p.sendActionBar("§cPrégénération §7[" + pb + "§7] §f§l» §e" + format.format(percentage) + "%"));
    }

    public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return com.google.common.base.Strings.repeat("" + completedColor + symbol, progressBars)
                + com.google.common.base.Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
}
