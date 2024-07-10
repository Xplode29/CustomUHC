package me.butter.impl.listeners;


import me.butter.api.UHCAPI;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.event.world.WorldInitEvent;

public class WorldListener implements Listener {

    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        if(event.getWorld().getName().equals("arena")) {
            event.getWorld().getPopulators().add(UHCAPI.getInstance().getWorldHandler().getOrePopulator());
        }
    }

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
        if(event.getWorld().getName().equals("arena")) {
            setBiomeCenter(event.getChunk());
        }
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if(event.getWorld().getName().equals("arena")) {
            setBiomeCenter(event.getChunk());
        }
    }

    private void setBiomeCenter(Chunk chunk) {
        Location center = new Location(chunk.getWorld(), 0.0D, 60.0D, 0.0D);
        if (chunk.getX() <= 60 && chunk.getZ() <= 60) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, 60, z);
                    if (block.getLocation().distance(center) <= 350.0D) block.setBiome(Biome.ROOFED_FOREST);
                    else if (block.getLocation().distance(center) <= 600.0D) block.setBiome(Biome.MEGA_TAIGA_HILLS);
                }
            }
        }
    }
}