package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.WorldUtils;
import me.butter.impl.events.EventUtils;
import me.butter.impl.events.custom.CustomBlockBreakEvent;
import net.minecraft.server.v1_8_R3.EntityExperienceOrb;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Random;

public class BlockEvents implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(event.getBlock() == null) return;
        if(event.getBlock().getType() == Material.AIR) return;

        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return;

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY) {
            if(!player.isOp()) {
                    event.setCancelled(true);
            }
        }
        else if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
                UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void overrideBlockBreak(BlockBreakEvent event) {
        if(event.getBlock() == null) return;
        if(event.getBlock().getType() == Material.AIR) return;

        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return;

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY) {
            if(!player.isOp()) {
                event.setCancelled(true);
            }
        }
        else if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
                UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            event.setCancelled(true);
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            CustomBlockBreakEvent blockBreakEvent = new CustomBlockBreakEvent(event, uhcPlayer);
            EventUtils.callEvent(blockBreakEvent);

            if (blockBreakEvent.isCancelled()) {
                event.setCancelled(true);
                return;
            }

            event.setExpToDrop(0);

            if(blockBreakEvent.getExpToDrop() > 0) {
                int value = (int) (blockBreakEvent.getExpToDrop() * (1 + (UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() / 100f)));
                World w = ((CraftWorld) blockBreakEvent.getBlockBroken().getWorld()).getHandle();
                w.addEntity(new EntityExperienceOrb(w, blockBreakEvent.getBlockBroken().getX(), blockBreakEvent.getBlockBroken().getY(), blockBreakEvent.getBlockBroken().getZ(), value));
            }

            if(blockBreakEvent.isModified()) {
                event.getBlock().setType(Material.AIR);
                Location dropLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX() + 0.5, event.getBlock().getY() + 0.5, event.getBlock().getZ() + 0.5);
                for(ItemStack item : blockBreakEvent.getDrops()) {
                    event.getBlock().getWorld().dropItem(dropLocation, item);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(CustomBlockBreakEvent event) {
        UHCPlayer uhcPlayer = event.getUhcPlayer();
        final Block block = event.getBlockBroken();

        if(block.getType() == Material.DIAMOND_ORE) {
            if(uhcPlayer.getDiamondMined() < UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit()) {
                uhcPlayer.setDiamondMined(uhcPlayer.getDiamondMined() + 1);
                uhcPlayer.sendActionBar("§c§lDiamants minés : §7" + uhcPlayer.getDiamondMined() + " / " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit());
            }
            else {
                event.setDrops(Collections.singletonList(new ItemStack(Material.GOLD_INGOT, 1)));
            }
        }

        if ((new Random()).nextInt(100) <= UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() && (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2)) {
            event.setDrops(Collections.singletonList(new ItemStack(Material.APPLE, 1)));
        }
        if ((new Random()).nextInt(100) <= UHCAPI.getInstance().getGameHandler().getWorldConfig().getFlintDropRate() && block.getType() == Material.GRAVEL) {
            event.setDrops(Collections.singletonList(new ItemStack(Material.FLINT, 1)));
        }

        event.setExpToDrop((int) (event.getExpToDrop() * (1 + (UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() / 100f))));
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        final Block b = event.getBlock();
        if ((new Random()).nextInt(100) <= UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() && b.getType() == Material.LEAVES) {
            b.setType(Material.AIR);
            b.getWorld().dropItemNaturally(b.getLocation(), new ItemStack(Material.APPLE, 1));
        }
    }
}
