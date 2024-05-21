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

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player.getUniqueId());

        if (uhcPlayer == null) {
            UHCAPI.getInstance().getPlayerHandler().addPlayer(player);
            uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        }

        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(GameScoreboard.class, uhcPlayer);
            UHCAPI.getInstance().getTabHandler().setPlayerTab(GameTab.class, uhcPlayer);
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.ENDING) {

        }
        else {
            UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(LobbyScoreboard.class, uhcPlayer);
            UHCAPI.getInstance().getTabHandler().setPlayerTab(LobbyTab.class, uhcPlayer);
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

        UHCPlayer uhcPlayer = UHCImpl.getInstance().getPlayerHandler().getUHCPlayer(player);

        UHCAPI.getInstance().getScoreboardHandler().removePlayerScoreboard(uhcPlayer);
        UHCAPI.getInstance().getTabHandler().removePlayerTab(uhcPlayer);

        if (uhcPlayer.getPlayerState().equals(PlayerState.DEAD) || uhcPlayer.getPlayerState().equals(PlayerState.IN_GAME)) {
            return;
        }

        UHCAPI.getInstance().getPlayerHandler().removePlayer(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCImpl.getInstance().getPlayerHandler().getUHCPlayer(player);

        uhcPlayer.setLocation(player.getLocation());
    }
}
