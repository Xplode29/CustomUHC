package me.butter.impl.world.pregen;

import me.butter.api.UHCAPI;
import me.butter.impl.UHCBase;
import me.butter.impl.structures.StructureBuilderTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class PregenTask extends BukkitRunnable {

    private static boolean finished;

    private final int startX, startZ;
    private int x, z;
    private double currentChunkLoad;
    private final double totalChunkToLoad;

    private final World world;

    public PregenTask(World world, int radius) {
        finished = false;
        radius += 64;
        this.totalChunkToLoad = Math.pow(radius, 2.0D) / 64.0D;
        this.currentChunkLoad = 0.0D;
        this.world = world;

        this.startX = -radius; this.startZ = -radius;
        this.x = this.startX; this.z = this.startZ;
    }

    @Override
    public void run() {
        for (int i = 0; i < 40 && !finished; i++) {
            Location loc = new Location(world, x, 0, z);
            loc.getWorld().getChunkAt(loc.getChunk().getX(), loc.getChunk().getZ()).load(true);
            x += 16;
            currentChunkLoad ++;
            if (x > -startX) {
                x = startX;
                z += 16;
                if (z > -startZ) {
                    this.world.getWorldBorder().setCenter(new Location(this.world, 0.0D, 0.0D, 0.0D));
                    this.world.getWorldBorder().setSize(UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() * 2);
                    this.world.getWorldBorder().setDamageAmount(0.0D);
                    finished = true;
                    cancel();
                    Bukkit.getScheduler().runTaskLater(UHCBase.getInstance(), StructureBuilderTask::new, 20);
                    return;
                }
            }
        }

        if(currentChunkLoad < totalChunkToLoad)
            UHCAPI.getInstance().getPlayerHandler().getAllPlayers().forEach(p -> p.sendActionBar(
                    "§8[§6§lPregen§8] §7Chargement des chunks : " + (int) ((currentChunkLoad / totalChunkToLoad) * 100) + "%"
            ));
    }
}
