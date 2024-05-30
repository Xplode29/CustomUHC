package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatSnippets;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.EventUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DamageHealthEvents implements Listener {

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            event.setCancelled(true);
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (UHCAPI.getInstance().getGameHandler().getGameConfig().isInvincibility()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(!UHCAPI.getInstance().getGameHandler().getGameConfig().isPVP()) {
                if (event.getDamager() instanceof Player) {
                    event.setCancelled(true);
                }
                else if(event.getDamager() instanceof Projectile) {
                    Projectile projectile = (Projectile) event.getDamager();
                    if(projectile.getShooter() instanceof Player) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void patchDamage(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            UHCPlayer uhcDamager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
            if (uhcDamager != null && uhcDamager.getPotion(PotionEffectType.INCREASE_DAMAGE) != null) {
                event.setDamage(event.getDamage() / 2.299999952316284D * (1 + (double) uhcDamager.getStrength() / 100));
            }
        }

        if(event.getEntity() instanceof Player) {
            UHCPlayer uhcVictim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());

            if (uhcVictim != null && uhcVictim.getPotion(PotionEffectType.DAMAGE_RESISTANCE) != null) {
                event.setDamage(event.getDamage() / 0.800000011920929D * (1 - (double) uhcVictim.getResi() / 100));
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
    public void overrideDeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage(null);

        Player victim = event.getEntity();
        if (victim == null) return;
        UHCPlayer uhcVictim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(victim);
        if (uhcVictim == null) return;

        UHCPlayer uhcKiller;
        if(event.getEntity().getKiller() != null) {
            uhcKiller = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getEntity().getKiller());
            if (uhcKiller != null) uhcKiller.addKilledPlayer(uhcVictim);
        } else {
            uhcKiller = null;
        }

        uhcVictim.setDeathLocation(victim.getLocation());
        uhcVictim.saveInventory();

        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        if(uhcVictim.getRole() != null) {
            for(Power power : uhcVictim.getRole().getPowers()) {
                if(power instanceof ItemPower) {
                    drops.remove(((ItemPower) power).getItem());
                }
            }
        }
        event.getDrops().clear();

        UHCPlayerDeathEvent deathEvent = new UHCPlayerDeathEvent(event, uhcVictim, uhcKiller);
        EventUtils.callEvent(deathEvent);

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> {
            victim.spigot().respawn();
            victim.teleport(uhcVictim.getDeathLocation());

            victim.setGameMode(GameMode.SPECTATOR);
            uhcVictim.setPlayerState(PlayerState.DEAD);
        }, 1L);

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> {
            if(uhcVictim.getPlayerState() != PlayerState.DEAD) return;
            if(!deathEvent.isCancelled()) {
                for (ItemStack item : drops) {
                    uhcVictim.getDeathLocation().getWorld().dropItemNaturally(uhcVictim.getDeathLocation(), item);
                }

                if(!UHCAPI.getInstance().getModuleHandler().hasModule() || !UHCAPI.getInstance().getModuleHandler().getModule().hasCustomDeath()) {
                    ChatSnippets.playerDeath(uhcVictim);

                    for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
                        uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.WITHER_DEATH, 6.0F, 1.0F);
                    }
                }
            }
            else {
                uhcVictim.revive();
            }
        }, 5 * 20L);
    }

    @EventHandler
    public void onPlayerDie(UHCPlayerDeathEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY ||
                UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
                UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            event.setCancelled(true);
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (!UHCAPI.getInstance().getGameHandler().getGameConfig().isPVP()) {
                event.setCancelled(true);
            }
        }

        if(event.getKiller() != null) {
            UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().sendMessage(ChatUtils.GLOBAL_INFO.getMessage(event.getKiller().getName() + " a tue " + event.getVictim().getName() + "."));
        }
        else if(event.getVictim().getPlayer() != null) {
            UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().sendMessage(ChatUtils.GLOBAL_INFO.getMessage(event.getVictim().getPlayer().getName() + " est mort par " + event.getVictim().getPlayer().getLastDamageCause().getCause().name() + "."));
        }
        else {
            UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().sendMessage(ChatUtils.GLOBAL_INFO.getMessage(event.getVictim().getPlayer().getName() + " est mort."));
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
