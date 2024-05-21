package me.butter.ninjago.listener;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.events.custom.DayNightChangeEvent;
import me.butter.impl.events.custom.EpisodeEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CycleEvents implements Listener {

    @EventHandler
    public void onDayNightChangeEvent(DayNightChangeEvent event) {
        if(event.isDay()) {
            Bukkit.broadcastMessage("§6§l✹ LE SOLEIL SE LEVE ✹");
            UHCAPI.getInstance().getWorldHandler().getWorld().setTime(0);
        } else {
            Bukkit.broadcastMessage("§9§l☾ LA NUIT SE COUCHE ☾");
            UHCAPI.getInstance().getWorldHandler().getWorld().setTime(13000);
        }
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(uhcPlayer.getPlayer() == null) continue;
            uhcPlayer.sendTitle("Episode " + event.getEpisode(), ChatColor.GOLD);
            uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_PLING, 6.0F, 1.0F);
        }
    }
}
