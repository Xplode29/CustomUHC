package me.butter.impl.listeners.old;

import me.butter.api.UHCAPI;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ChatUtils;
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

        UHCPlayer uhcVictim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(victim);

        if (!uhcVictim.getPlayerState().equals(PlayerState.IN_GAME)) {
            return;
        }

        UHCPlayer uhcKiller = null;

        if (killer != null) {
            uhcKiller = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(killer);
        }

        if (uhcKiller != null) {
            uhcKiller.addKilledPlayer(uhcVictim);
        }

        uhcVictim.setDeathLocation(victim.getLocation());
        uhcVictim.saveInventory();

        UHCPlayerDeathEvent deathEvent = new UHCPlayerDeathEvent(event, uhcVictim, uhcKiller);

        EventUtils.callEvent(deathEvent);

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> {
            victim.spigot().respawn();
            victim.teleport(uhcVictim.getDeathLocation());

            if (!UHCAPI.getInstance().getGameHandler().getGameConfig().isPvp()) {
                uhcVictim.loadInventory();
                uhcVictim.setCanPickItems(false);

                Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(uhcVictim.getName() + " a été réanimé !"));
                for (Entity entity : uhcVictim.getDeathLocation().getWorld().getNearbyEntities(uhcVictim.getDeathLocation(), 5, 100, 5)) {
                    if (!(entity instanceof Item)) {
                        continue;
                    }
                    entity.remove();
                }
                Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> {
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

        Bukkit.broadcastMessage(ChatUtils.SEPARATOR.getPrefix());
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(uhcVictim.getName() + " est mort !");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatUtils.SEPARATOR.getPrefix());
    } //Done
}
