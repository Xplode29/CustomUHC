package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.scoreboard.list.LobbyScoreboard;
import me.butter.impl.tab.list.GameTab;
import me.butter.impl.tab.list.LobbyTab;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class UHCPlayerListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player.getUniqueId());

        if (uhcPlayer == null) {
            UHCAPI.get().getPlayerHandler().addPlayer(player);
            uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);
        }

        if(UHCAPI.get().getGameHandler().getGameState() == GameState.IN_GAME) {
            UHCAPI.get().getScoreboardHandler().setPlayerScoreboard(GameScoreboard.class, uhcPlayer);
            UHCAPI.get().getTabHandler().setPlayerTab(GameTab.class, uhcPlayer);
        }
        else if (UHCAPI.get().getGameHandler().getGameState() == GameState.ENDING) {

        }
        else {
            UHCAPI.get().getScoreboardHandler().setPlayerScoreboard(LobbyScoreboard.class, uhcPlayer);
            UHCAPI.get().getTabHandler().setPlayerTab(LobbyTab.class, uhcPlayer);
        }

        uhcPlayer.setName(player.getName());

        event.setJoinMessage(null);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        UHCPlayer uhcPlayer = UHCImpl.get().getPlayerHandler().getUHCPlayer(player);

        UHCAPI.get().getScoreboardHandler().removePlayerScoreboard(uhcPlayer);
        UHCAPI.get().getTabHandler().removePlayerTab(uhcPlayer);

        if (uhcPlayer.getPlayerState().equals(PlayerState.DEAD) || uhcPlayer.getPlayerState().equals(PlayerState.IN_GAME)) {
            return;
        }

        UHCAPI.get().getPlayerHandler().removePlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCImpl.get().getPlayerHandler().getUHCPlayer(player);

        uhcPlayer.setLocation(player.getLocation());
    }
}
