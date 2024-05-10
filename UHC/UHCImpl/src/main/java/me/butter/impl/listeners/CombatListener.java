package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.chat.ChatPrefixes;
import me.butter.impl.events.EventUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class CombatListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();

        if (victim == null) {
            return;
        }

        UHCPlayer uhcVictim = UHCAPI.get().getPlayerHandler().getUHCPlayer(victim);

        if (!uhcVictim.getPlayerState().equals(PlayerState.IN_GAME)) {
            return;
        }

        UHCPlayer uhcKiller = null;

        if (killer != null) {
            uhcKiller = UHCAPI.get().getPlayerHandler().getUHCPlayer(killer);
        }

        if (uhcKiller != null) {
            uhcKiller.addKilledPlayer(uhcVictim);
        }

        uhcVictim.setDeathLocation(victim.getLocation());
        uhcVictim.saveInventory();

        UHCPlayerDeathEvent deathEvent = new UHCPlayerDeathEvent(event, uhcVictim, uhcKiller);

        EventUtils.callEventInModule(deathEvent);

        Bukkit.getScheduler().runTaskLater(UHCAPI.get(), () -> {
            victim.spigot().respawn();
            victim.teleport(uhcVictim.getDeathLocation());

            if (!UHCAPI.get().getGameHandler().getGameConfig().isPvp()) {
                uhcVictim.loadInventory();
                uhcVictim.setCanPickItems(false);

                Bukkit.broadcastMessage(ChatPrefixes.GLOBAL_INFO.getMessage(uhcVictim.getName() + " a été réanimé !"));
                for (Entity entity : uhcVictim.getDeathLocation().getWorld().getNearbyEntities(uhcVictim.getDeathLocation(), 5, 100, 5)) {
                    if (!(entity instanceof Item)) {
                        continue;
                    }
                    entity.remove();
                }
                Bukkit.getScheduler().runTaskLater(UHCAPI.get(), () -> {
                    uhcVictim.setCanPickItems(true);
                }, 10);
            } else {
                victim.setGameMode(GameMode.SPECTATOR);
                uhcVictim.setPlayerState(PlayerState.DEAD);
            }
        }, 1L);

        if (deathEvent.isCancelled()) {
            return;
        }

        Bukkit.broadcastMessage(ChatPrefixes.SEPARATOR.getPrefix());
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatPrefixes.GLOBAL_INFO.getMessage("§c" + uhcVictim.getName() + "§r est mort !"));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatPrefixes.SEPARATOR.getPrefix());
    }
}
