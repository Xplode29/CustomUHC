package me.butter.impl.scenario.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TimberScenario extends AbstractScenario {

    public TimberScenario() {
        super("Timber", Material.DIAMOND_AXE);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock() == null || event.getBlock().getDrops() == null) {
            return;
        }

        if(UHCAPI.get().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(event.getBlock().getType() == Material.LOG || event.getBlock().getType() == Material.LOG_2) {
                new breakRunnable(event.getBlock());
            }
        }
    }

    private static class breakRunnable extends BukkitRunnable {
        private final Block block;

        public breakRunnable(Block block) {
            this.block = block;
            this.runTaskLater(UHCAPI.get(), 1);
        }

        @Override
        public void run() {
            for(Block b : getNearbyBlocks(block.getLocation(), 1)) {
                if(b.getType() == Material.LOG || b.getType() == Material.LOG_2) {
                    b.breakNaturally();
                    new breakRunnable(b);
                }
            }
            cancel();
        }
    }

    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<>();
        for(double x = location.getX() - radius; x <= location.getX() + radius; x++) {
            for(double y = location.getY() - radius; y <= location.getY() + radius; y++) {
                for(double z = location.getZ() - radius; z <= location.getZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt((int) x, (int) y, (int) z));
                }
            }
        }
        return blocks;
    }
}
