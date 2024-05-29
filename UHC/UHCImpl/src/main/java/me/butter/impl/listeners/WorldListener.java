package me.butter.impl.listeners;


import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.events.custom.DayNightChangeEvent;
import me.butter.impl.events.custom.EpisodeEvent;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        if(event.getWorld().getName() == "arena") {
            setBiomeCenter(event.getChunk());
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if(event.getWorld().getName() == "arena") {
            setBiomeCenter(event.getChunk());
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkLoadEvent event) {
        if(event.getWorld().getName() == "world" && Math.abs(event.getChunk().getX()) <= 60 && Math.abs(event.getChunk().getZ()) <= 60) {
            event.getChunk().load();
        }
    }

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