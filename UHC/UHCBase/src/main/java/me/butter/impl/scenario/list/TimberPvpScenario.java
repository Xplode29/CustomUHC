package me.butter.impl.scenario.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.impl.events.custom.CustomBlockBreakEvent;
import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TimberPvpScenario extends AbstractScenario {

    public TimberPvpScenario() {
        super("Timber PVP", Material.DIAMOND_AXE);
    }

    @EventHandler
    public void onBlockBreak(CustomBlockBreakEvent event) {
        if(UHCAPI.getInstance().getGameHandler().getGameConfig().isPVP()) return;

        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(event.getBlockBroken().getType() == Material.LOG || event.getBlockBroken().getType() == Material.LOG_2) {
                new breakRunnable(event.getBlockBroken(), 64);
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Les arbres sont minés en un coup.",
                "Se désactive au PVP."
        };
    }

    private static class breakRunnable extends BukkitRunnable {
        private final Block block;

        int amount;

        public breakRunnable(Block block, int amount) {
            this.block = block;
            this.amount = amount;

            this.runTaskLater(UHCAPI.getInstance(), 3);
        }

        @Override
        public void run() {
            if(amount == 0) {
                cancel();
                return;
            }

            for(Block b : getNearbyBlocks(block.getLocation(), 1)) {
                if(b.getType() == Material.LOG || b.getType() == Material.LOG_2) {
                    b.breakNaturally();
                    new breakRunnable(b, amount - 1);
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
