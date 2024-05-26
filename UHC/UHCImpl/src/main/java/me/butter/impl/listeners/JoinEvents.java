package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.BlockUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.api.utils.ParticleUtils;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.scoreboard.list.LobbyScoreboard;
import me.butter.impl.tab.list.GameTab;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinEvents implements Listener {

    private boolean generate;
    private Location spawnLocation;

    public JoinEvents() {
        this.generate = false;
        spawnLocation = new Location(Bukkit.getWorld("world"), 0.0D, 201, 0.0D);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        if (!generate) {
            //Build the spawn
            int spawnSize = 40, spawnHeight = 10;
            BlockUtils.fillBlocks(spawnLocation.getWorld(), spawnLocation.getBlockX() - spawnSize/2, spawnLocation.getBlockY() - 1, spawnLocation.getBlockZ() - spawnSize/2, spawnSize, spawnHeight, spawnSize, Material.BARRIER);
            BlockUtils.fillBlocks(spawnLocation.getWorld(), spawnLocation.getBlockX() - spawnSize/2 + 1, spawnLocation.getBlockY(), spawnLocation.getBlockZ() - spawnSize/2 + 1, spawnSize - 2, spawnHeight - 1, spawnSize - 2, Material.AIR);

            generate = true;
        }

        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player.getUniqueId());
        if (uhcPlayer == null) {
            UHCAPI.getInstance().getPlayerHandler().addPlayer(player);
            uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        }
        uhcPlayer.setName(player.getName());

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY) {
            uhcPlayer.clearPlayer();
            uhcPlayer.setPlayerState(PlayerState.IN_LOBBY);

            Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.teleport(spawnLocation), 2);

            UHCAPI.getInstance().getItemHandler().giveLobbyItems(uhcPlayer);

            UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(LobbyScoreboard.class, uhcPlayer);
            UHCAPI.getInstance().getTabHandler().setPlayerTab(GameTab.class, uhcPlayer);

            ParticleUtils.tornadoEffect(uhcPlayer.getPlayer(), Color.fromRGB(255, 255, 255));
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING) {
            if(uhcPlayer.getPlayerState().equals(PlayerState.IN_SPEC)) {
                Location teleport = new Location(UHCAPI.getInstance().getWorldHandler().getWorld(), 0.0D, 100, 0.0D);

                player.teleport(teleport);
                player.setGameMode(GameMode.SPECTATOR);
            }
            else {
                player.teleport(uhcPlayer.getSpawnLocation());
                player.setGameMode(GameMode.SURVIVAL);
            }

            UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(GameScoreboard.class, uhcPlayer);
            UHCAPI.getInstance().getTabHandler().setPlayerTab(GameTab.class, uhcPlayer);
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING ||
                UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME
        ) {
            if(uhcPlayer.getPlayerState().equals(PlayerState.IN_SPEC)) {
                Location teleport = new Location(UHCAPI.getInstance().getWorldHandler().getWorld(), 0.0D, 100, 0.0D);

                player.teleport(teleport);
                player.setGameMode(GameMode.SPECTATOR);
            }
        }

        event.setJoinMessage(null);
        Bukkit.broadcastMessage(ChatUtils.JOINED.getMessage(
                player.getDisplayName() + " [" +
                        (UHCAPI.getInstance().getPlayerHandler().getPlayers().size() > UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                        UHCAPI.getInstance().getPlayerHandler().getPlayers().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.DARK_GRAY + "] "
        ));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if (uhcPlayer == null) return;

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY) {
            UHCAPI.getInstance().getItemHandler().removeLobbyItems(uhcPlayer);
        }

        UHCAPI.getInstance().getScoreboardHandler().removePlayerScoreboard(uhcPlayer);
        UHCAPI.getInstance().getTabHandler().removePlayerTab(uhcPlayer);

        event.setQuitMessage(null);
        Bukkit.broadcastMessage(ChatUtils.LEFT.getMessage(
                player.getDisplayName() + " [" +
                        ((UHCAPI.getInstance().getPlayerHandler().getPlayers().size() - 1) > UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                        (UHCAPI.getInstance().getPlayerHandler().getPlayers().size() - 1) + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.DARK_GRAY + "] "
        ));

        UHCAPI.getInstance().getPlayerHandler().removePlayer(player);
    }
}
