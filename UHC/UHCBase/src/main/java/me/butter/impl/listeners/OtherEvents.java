package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.DayNightChangeEvent;
import me.butter.impl.events.custom.EpisodeEvent;
import net.minecraft.server.v1_8_R3.EntityPotion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Random;

public class OtherEvents implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if (player == null) return;

        if(!player.isAbleToMove() && (event.getTo().getX() != event.getFrom().getX() || event.getTo().getZ() != event.getFrom().getZ())) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        if(
            event.getEntity() instanceof Monster &&
            event.getEntity().getLocation().getY() > 70 &&
            event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if (player == null) return;

        if (!UHCAPI.getInstance().getGameHandler().getGameConfig().isChatEnabled() && !event.isCancelled()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Le chat est actuellement désactivé."));
            event.setCancelled(true);
            return;
        }

        String prefix = ChatColor.GRAY + player.getName();
        if(player.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())) {
            prefix = "§lHOST " + player.getName() + "§r";
        }
        else if(UHCAPI.getInstance().getGameHandler().getGameConfig().getCoHosts().contains(player)) {
            prefix = "§lCO-HOST " + player.getName() + "§r";
        }
        event.setFormat(prefix + ChatUtils.CHAT + ChatColor.translateAlternateColorCodes('&', event.getMessage()));
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
}
