package me.butter.ninjago.listener;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.module.camp.Camp;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DamageEvents implements Listener {

    @EventHandler
    public void onPlayerDies(UHCPlayerDeathEvent event) {
        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME && !UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().isEmpty()) {
            Camp camp = UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().get(0).getRole().getCamp();

            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                if(uhcPlayer.getPlayer() == null || uhcPlayer.getRole() == null) continue;
                if(camp != uhcPlayer.getRole().getCamp()) return;
            }

            Bukkit.broadcastMessage("Le camp " + camp.getName() + " a gagne");
            UHCAPI.getInstance().getGameHandler().setGameState(GameState.ENDING);
        }
    }
}
