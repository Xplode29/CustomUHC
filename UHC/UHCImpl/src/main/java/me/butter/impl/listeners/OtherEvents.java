package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
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
            if (!UHCAPI.getInstance().getGameHandler().getGameConfig().isChatEnabled() ||
                    UHCAPI.getInstance().getGameHandler().getGameConfig().isPVP()) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Le chat est actuellement désactivé."));
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onAcquireAchievement(PlayerAchievementAwardedEvent event) {
        event.setCancelled(true);
    }

    public static boolean isOutsideOfBorder(Location location) {
        WorldBorder border = location.getWorld().getWorldBorder();
        double x = location.getX();
        double z = location.getZ();
        double size = border.getSize() / 2;
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }
}
