package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ChatUtils;
import me.butter.impl.events.EventUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class DamageHealthEvents implements Listener {

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY ||
                UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
                UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
                return;
            }
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (event.getEntity() instanceof Player) {
                if (UHCAPI.getInstance().getGameHandler().getGameConfig().isInvincibility()) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            if (event.getEntity() instanceof Player) {
                event.setCancelled(true);
            }
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (event.getEntity() instanceof Player) {
                if (UHCAPI.getInstance().getGameHandler().getGameConfig().isInvincibility()) {
                    event.setCancelled(true);
                    return;
                }
                else {
                    if (event.getDamager() instanceof Player) {
                        Player victim = (Player) event.getEntity();
                        Player damager = (Player) event.getDamager();

                        UHCPlayer uhcVictim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(victim);
                        UHCPlayer uhcDamager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(damager);

                        if (uhcDamager.getPotion(PotionEffectType.INCREASE_DAMAGE) != null) {
                            event.setDamage(event.getDamage() / 2.299999952316284D * (1 + (double) uhcDamager.getStrength() / 100));
                        }

                        if (uhcVictim.getPotion(PotionEffectType.DAMAGE_RESISTANCE) != null) {
                            event.setDamage(event.getDamage() / 0.800000011920929D * (1 - (double) uhcVictim.getResi() / 100));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (event.getEntity() instanceof Enderman) {
                event.getDrops().clear();

                final Block b = event.getEntity().getLocation().getBlock();
                final Location loc = new Location(b.getWorld(), b.getLocation().getBlockX() + 0.0, b.getLocation().getBlockY() + 0.0, b.getLocation().getBlockZ() + 0.0);

                if ((new Random()).nextInt(100) <= UHCAPI.getInstance().getGameHandler().getWorldConfig().getEnderPearlDropRate()) {
                    b.getWorld().dropItemNaturally(loc, new ItemStack(Material.ENDER_PEARL, 1));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDie(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        Player victim = event.getEntity();
        if (victim == null) return;
        UHCPlayer uhcVictim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(victim);

        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (!uhcVictim.getPlayerState().equals(PlayerState.IN_GAME)) return;

            UHCPlayer uhcKiller = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getEntity().getKiller());
            if (uhcKiller != null) uhcKiller.addKilledPlayer(uhcVictim);

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

            if (deathEvent.isCancelled()) return;

            Bukkit.broadcastMessage(ChatUtils.SEPARATOR.getPrefix());
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(uhcVictim.getName() + " est mort !");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(ChatUtils.SEPARATOR.getPrefix());
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onRegainHeal(EntityRegainHealthEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING) {
                event.setCancelled(true);
            }
        }
    }
}
