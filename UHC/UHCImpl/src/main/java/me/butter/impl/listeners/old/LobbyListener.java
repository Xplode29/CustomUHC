package me.butter.impl.listeners.old;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.BlockUtils;
import me.butter.api.utils.ChatUtils;
import me.butter.api.utils.ParticleUtils;
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
import org.bukkit.potion.PotionEffect;

public class LobbyListener implements Listener {

    private boolean generate;
    private Location spawnLocation;

    public LobbyListener() {
        this.generate = false;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        Player player = event.getPlayer();
        if (player == null) return;

        if (!generate) {
            spawnLocation = new Location(UHCAPI.getInstance().getWorldHandler().getWorld(), 0.0D, 201, 0.0D);
            //Build the spawn
            int spawnSize = 40, spawnHeight = 10;
            BlockUtils.fillBlocks(spawnLocation.getWorld(), spawnLocation.getBlockX() - spawnSize/2, spawnLocation.getBlockY() - 1, spawnLocation.getBlockZ() - spawnSize/2, spawnSize, spawnHeight, spawnSize, Material.BARRIER);
            BlockUtils.fillBlocks(spawnLocation.getWorld(), spawnLocation.getBlockX() - spawnSize/2 + 1, spawnLocation.getBlockY(), spawnLocation.getBlockZ() - spawnSize/2 + 1, spawnSize - 2, spawnHeight - 1, spawnSize - 2, Material.AIR);

            generate = true;
        }

        player.setGameMode(GameMode.SURVIVAL);
        player.setMaxHealth(20);
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
        for(PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.teleport(spawnLocation), 1);

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if (uhcPlayer == null) return;

        uhcPlayer.clearInventory();

        UHCAPI.getInstance().getItemHandler().giveLobbyItems(uhcPlayer);

        ParticleUtils.tornadoEffect(uhcPlayer.getPlayer(), Color.fromRGB(255, 255, 255));

        uhcPlayer.setPlayerState(PlayerState.IN_LOBBY);
        Bukkit.broadcastMessage(ChatUtils.JOINED.getMessage(
                 player.getDisplayName() + ChatColor.WHITE + " a rejoint le lobby [" +
                         (UHCAPI.getInstance().getPlayerHandler().getPlayers().size() > UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                         UHCAPI.getInstance().getPlayerHandler().getPlayers().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.WHITE + "] "
        ));
    } //Done

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) return;

        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if (uhcPlayer == null) return;

        UHCAPI.getInstance().getItemHandler().removeLobbyItems(uhcPlayer);

        Bukkit.broadcastMessage(ChatUtils.LEFT.getMessage(
                 player.getDisplayName() + ChatColor.WHITE + " a quitté le lobby [" +
                         ((UHCAPI.getInstance().getPlayerHandler().getPlayers().size() - 1) > UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                         (UHCAPI.getInstance().getPlayerHandler().getPlayers().size() - 1) + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.WHITE + "] "
        ));
    } //Done

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        event.setCancelled(true);
    } //Done

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        event.setCancelled(true);
    } //Done

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        event.setCancelled(true);
    } //Done

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        if (event.getPlayer().isOp() && event.getBlock().getType() != Material.GLASS) {
            return;
        }

        event.setCancelled(true);
    } //Done

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.LOBBY)) {
            return;
        }

        if (event.getPlayer().isOp() && event.getBlock().getType() != Material.GLASS) {
            return;
        }

        event.setCancelled(true);
    } //Done
}
