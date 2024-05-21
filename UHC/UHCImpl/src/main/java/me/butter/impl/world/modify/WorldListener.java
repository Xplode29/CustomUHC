package me.butter.impl.world.modify;


import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onAcquireAchievement(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        setBiomeCenter(event.getChunk());
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        setBiomeCenter(event.getChunk());
    }

//    @EventHandler
//    public void onChunkUnLoad(ChunkUnloadEvent event) {
//        event.setCancelled(true);
//    }

    private void setBiomeCenter(Chunk chunk) {
        Location center = new Location(chunk.getWorld(), 0.0D, 60.0D, 0.0D);
        if (chunk.getX() <= 60 && chunk.getZ() <= 60)
            for (int x = 0; x < 16; x++)
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, 60, z);
                    if (block.getLocation().distance(center) <= 350.0D) block.setBiome(Biome.ROOFED_FOREST);
                    else if (block.getLocation().distance(center) <= 600.0D) block.setBiome(Biome.MEGA_TAIGA_HILLS);
                }
    }
}