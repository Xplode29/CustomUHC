package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.DayNightChangeEvent;
import me.butter.impl.events.custom.EpisodeEvent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class OtherEvents implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (isOutsideOfBorder(event.getTo())) {
                player.teleport(new Location(player.getWorld(), 0, 90, 0));
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (!event.isCancelled() &&
                !UHCAPI.getInstance().getGameHandler().getGameConfig().isChatEnabled()
            ) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Le chat est actuellement désactivé."));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onAcquireAchievement(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDayNightChangeEvent(DayNightChangeEvent event) {
        if(UHCAPI.getInstance().getModuleHandler().hasModule()) return;
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
        if(UHCAPI.getInstance().getModuleHandler().hasModule()) return;
        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(uhcPlayer.getPlayer() == null) continue;
            uhcPlayer.sendTitle("Episode " + event.getEpisode(), ChatColor.GOLD);
            uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_PLING, 6.0F, 1.0F);
        }
    }

    public static boolean isOutsideOfBorder(Location location) {
        WorldBorder border = location.getWorld().getWorldBorder();
        double x = location.getX();
        double z = location.getZ();
        double size = border.getSize() / 2;
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }
}
