package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.BlockUtils;
import me.butter.api.utils.ChatUtils;
import me.butter.api.utils.ParticleUtils;
import me.butter.impl.item.list.GrapplingItem;
import me.butter.impl.item.list.MenuItem;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LobbyListener implements Listener {

    private boolean generate;
    private final Location spawnLocation;

    public LobbyListener() {
        this.generate = false;
        this.spawnLocation = new Location(Bukkit.getWorld("world"), 0.0D, 201, 0.0D);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        if (!generate) {
            //Build the spawn
            int spawnSize = 40, spawnHeight = 10;
            BlockUtils.fillBlocks(spawnLocation.getWorld(), spawnLocation.getBlockX() - spawnSize/2, spawnLocation.getBlockY() - 1, spawnLocation.getBlockZ() - spawnSize/2, spawnSize, spawnHeight, spawnSize, Material.GLASS);
            BlockUtils.fillBlocks(spawnLocation.getWorld(), spawnLocation.getBlockX() - spawnSize/2 + 1, spawnLocation.getBlockY(), spawnLocation.getBlockZ() - spawnSize/2 + 1, spawnSize - 2, spawnHeight - 2, spawnSize - 2, Material.AIR);

            generate = true;
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.getInventory().clear();
        player.teleport(spawnLocation);

        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);
        if (uhcPlayer == null) return;

        UHCAPI.get().getItemHandler().giveLobbyItems(uhcPlayer);

        ParticleUtils.tornadoEffect(uhcPlayer.getPlayer(), Color.fromRGB(255, 255, 255));

        uhcPlayer.setPlayerState(PlayerState.IN_LOBBY);
        Bukkit.broadcastMessage(ChatUtils.JOINED.getMessage(
                 player.getDisplayName() + ChatColor.WHITE + " a rejoint le lobby [" +
                         (UHCAPI.get().getPlayerHandler().getPlayers().size() > UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                         UHCAPI.get().getPlayerHandler().getPlayers().size() + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.WHITE + "] "
        ));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.LOBBY)) return;

        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);
        if (uhcPlayer == null) return;

        UHCAPI.get().getItemHandler().removeLobbyItems(uhcPlayer);

        Bukkit.broadcastMessage(ChatUtils.LEFT.getMessage(
                 player.getDisplayName() + ChatColor.WHITE + " a quitté le lobby [" +
                         ((UHCAPI.get().getPlayerHandler().getPlayers().size() - 1) > UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                         (UHCAPI.get().getPlayerHandler().getPlayers().size() - 1) + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.WHITE + "] "
        ));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        if (event.getPlayer().isOp() && event.getBlock().getType() != Material.GLASS) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        if (event.getPlayer().isOp() && event.getBlock().getType() != Material.GLASS) {
            return;
        }

        event.setCancelled(true);
    }
}
