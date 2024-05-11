package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeleportListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.TELEPORTING)) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        Location teleport = new Location(UHCAPI.get().getWorldHandler().getWorld(), 0.0D, 100, 0.0D);

        player.teleport(teleport);
        player.setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.TELEPORTING)) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);

        if (!uhcPlayer.getPlayerState().equals(PlayerState.IN_LOBBY)) {
            return;
        }

        Bukkit.broadcastMessage(ChatUtils.LEFT.getMessage(
                player.getDisplayName() + ChatColor.WHITE + " s'est déconnecté pendant le lancement de la partie."
        ));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.TELEPORTING)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.TELEPORTING)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.TELEPORTING)) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);

        if (!uhcPlayer.getPlayerState().equals(PlayerState.IN_LOBBY)) {
            return;
        }

        if (player.getLocation().getY() > 180) {
            return;
        }

        player.teleport(uhcPlayer.getSpawnLocation());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.TELEPORTING)) {
            return;
        }

        event.setCancelled(true);
    }
}
