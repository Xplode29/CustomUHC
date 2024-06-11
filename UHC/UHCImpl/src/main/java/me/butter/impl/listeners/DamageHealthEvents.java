package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.module.camp.Camp;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatSnippets;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.EventUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import net.minecraft.server.v1_8_R3.EntityLiving;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DamageHealthEvents implements Listener {

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            event.setCancelled(true);
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (UHCAPI.getInstance().getGameHandler().getGameConfig().isInvincibility()) {
                event.setCancelled(true);
            } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL && uhcPlayer != null && uhcPlayer.hasNoFall()) {
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
        //After Reduction (Resistance)
        if(event.getEntity() instanceof Player) {
            UHCPlayer uhcVictim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
            if(uhcVictim != null) {
                PotionEffect potionEffect = uhcVictim.getPlayer().getActivePotionEffects().stream().filter(
                        e -> e.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)
                ).findFirst().orElse(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 0, -1));

                event.setDamage(
                        EntityDamageEvent.DamageModifier.RESISTANCE,
                        event.getDamage(EntityDamageEvent.DamageModifier.RESISTANCE) / (1 - (potionEffect.getAmplifier() + 1.0) * (1 / 5.0))
                );
            }
        }

        //Before Reduction (Strength / Weakness)
        if(event.getDamager() instanceof Player) {
            UHCPlayer uhcDamager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
            if(uhcDamager != null) {
                //Remove damages from others
                double damageBeforeReduction = event.getDamage();
                ItemStack itemStack = uhcDamager.getPlayer().getInventory().getItemInHand();
                if(itemStack != null && itemStack.containsEnchantment(Enchantment.DAMAGE_ALL)) {
                    damageBeforeReduction -= itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 1.25;
                }

                boolean isCrit = uhcDamager.getPlayer().getFallDistance() > 0.0F && !uhcDamager.getPlayer().isOnGround() && uhcDamager.getPlayer() instanceof EntityLiving && !uhcDamager.getPlayer().hasPotionEffect(PotionEffectType.BLINDNESS) && uhcDamager.getPlayer().getVehicle() == null;
                if(isCrit) damageBeforeReduction /= 1.5;

                //Modify Damages with potions
                //Remove Strength
                PotionEffect potionEffect = uhcDamager.getPlayer().getActivePotionEffects().stream().filter(
                        e -> e.getType().equals(PotionEffectType.INCREASE_DAMAGE)
                ).findFirst().orElse(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 0, -1));
                damageBeforeReduction /= (1 + 1.3 * (potionEffect.getAmplifier() + 1.0));

                //Remove Weakness
                potionEffect = uhcDamager.getPlayer().getActivePotionEffects().stream().filter(
                        e -> e.getType().equals(PotionEffectType.WEAKNESS)
                ).findFirst().orElse(new PotionEffect(PotionEffectType.WEAKNESS, 0, -1));
                damageBeforeReduction += (0.5 * (potionEffect.getAmplifier() + 1.0));

                //Add damages from others
                if(isCrit) damageBeforeReduction *= 1.5;

                if(itemStack != null && itemStack.containsEnchantment(Enchantment.DAMAGE_ALL)) {
                    damageBeforeReduction += itemStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) * 1.25;
                }

                event.setDamage(damageBeforeReduction);
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

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> {
            victim.spigot().respawn();
            victim.teleport(uhcVictim.getDeathLocation());

            victim.setGameMode(GameMode.SPECTATOR);
            uhcVictim.setPlayerState(PlayerState.DEAD);

            EventUtils.callEvent(deathEvent);
        }, 1L);

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> {
            if(uhcVictim.getPlayerState() != PlayerState.DEAD) return;
            if(!deathEvent.isCancelled()) {
                for (ItemStack item : drops) {
                    uhcVictim.getDeathLocation().getWorld().dropItemNaturally(uhcVictim.getDeathLocation(), item);
                }

                if(deathEvent.showDeath) {
                    if(!UHCAPI.getInstance().getModuleHandler().hasModule() || !UHCAPI.getInstance().getModuleHandler().getModule().hasCustomDeath()) {
                        ChatSnippets.playerDeath(uhcVictim);

                        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
                            if(uhcPlayer.getPlayer() == null) continue;
                            uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.WITHER_DEATH, 6.0F, 1.0F);
                        }
                    }
                }

                if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
                    if(UHCAPI.getInstance().getModuleHandler().hasModule()) {
                        if(!UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().isEmpty()) {
                            List<Camp> camps = new ArrayList<>();

                            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                                if(uhcPlayer.getPlayer() == null || uhcPlayer.getRole() == null) continue;
                                Camp camp = uhcPlayer.getRole().getCamp();
                                if(!camps.contains(camp)) {
                                    camps.add(camp);
                                }
                            }

                            if(camps.size() > 1) return;

                            Camp camp = camps.get(0);

                            if(camp.isSolo() && UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() > 1) return;

                            Bukkit.broadcastMessage(ChatUtils.SEPARATOR + "");
                            Bukkit.broadcastMessage("");
                            if(camp.isSolo()) {
                                UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().get(0);
                                Bukkit.broadcastMessage("Le joueur " + camp.getPrefix() + uhcPlayer.getName() + " a gagne !");
                            }
                            else {
                                Bukkit.broadcastMessage("Les " + camp.getPrefix() + camp.getName() + "§r ont gagnes !");
                            }
                            Bukkit.broadcastMessage("");
                            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
                                if(uhcPlayer.getRole() != null && (
                                        uhcPlayer.getPlayerState() == PlayerState.IN_GAME ||
                                                uhcPlayer.getPlayerState() == PlayerState.DEAD
                                )) {
                                    Bukkit.broadcastMessage(uhcPlayer.getName() + " (" + uhcPlayer.getRole().getCamp().getPrefix() + uhcPlayer.getRole().getName() + "§r) : " + uhcPlayer.getKilledPlayers().size() + " kill(s)");
                                }
                            }
                            Bukkit.broadcastMessage("");
                            Bukkit.broadcastMessage(ChatUtils.SEPARATOR + "");

                            UHCAPI.getInstance().getGameHandler().setGameState(GameState.ENDING);
                        }
                    }
                    else if(UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() < 2) {
                        UHCAPI.getInstance().getGameHandler().setGameState(GameState.ENDING);
                        UHCPlayer winner = UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().get(0);

                        Bukkit.broadcastMessage(ChatUtils.SEPARATOR + "");
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage("Le joueur " + winner.getName() + " a gagne !");
                        Bukkit.broadcastMessage("");
                        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
                            if(uhcPlayer.getRole() != null && (
                                    uhcPlayer.getPlayerState() == PlayerState.IN_GAME ||
                                            uhcPlayer.getPlayerState() == PlayerState.DEAD
                            )) {
                                Bukkit.broadcastMessage(uhcPlayer.getName() + " : " + uhcPlayer.getKilledPlayers().size() + " kill(s)");
                            }
                        }
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(ChatUtils.SEPARATOR + "");
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
