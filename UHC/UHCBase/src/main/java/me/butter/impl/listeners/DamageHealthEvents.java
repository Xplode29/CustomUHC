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
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DamageHealthEvents implements Listener {

    @EventHandler
    public void onEntityDamaged(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());

        if(!player.isAbleToMove()) {
            event.setCancelled(true);
            return;
        }

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            event.setCancelled(true);
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if (UHCAPI.getInstance().getGameHandler().getGameConfig().isInvincibility()) {
                event.setCancelled(true);
            } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL && player.hasNoFall()) {
                event.setDamage(0);
            }
        }
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            UHCPlayer attacker = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
            if(attacker != null) {
                event.setDamage(event.getDamage() * (1 + attacker.getStrength() / 100.0f));
            }
        }

        if(event.getEntity() instanceof Player) {
            UHCPlayer victim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
            if(victim != null) {
                event.setDamage(event.getDamage() * (1 - victim.getResi() / 100.0f));

                if(!victim.isAbleToMove()) {
                    event.setCancelled(true);
                    return;
                }
            }

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

            EventUtils.callEvent(deathEvent);
        }, 1L);

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> {
            if(!deathEvent.isCancelled()) {
                uhcVictim.setPlayerState(PlayerState.DEAD);

                if(uhcVictim.getPlayer() != null) {
                    for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                        if(uhcPlayer.getPlayer() == null) continue;
                        uhcPlayer.getPlayer().hidePlayer(uhcVictim.getPlayer());
                    }

                    for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersConnected()) {
                        if(uhcPlayer.getPlayer() == null) continue;
                        uhcVictim.getPlayer().showPlayer(uhcPlayer.getPlayer());
                    }
                }

                for (ItemStack item : drops) {
                    uhcVictim.getDeathLocation().getWorld().dropItemNaturally(uhcVictim.getDeathLocation(), item);
                }

                if(deathEvent.showDeath) {
                    if(!UHCAPI.getInstance().getModuleHandler().hasModule() || !UHCAPI.getInstance().getModuleHandler().getModule().hasCustomDeath()) {
                        ChatSnippets.playerDeath(uhcVictim);

                        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersConnected()) {
                            if(uhcPlayer.getPlayer() == null) continue;
                            uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.WITHER_DEATH, 6.0F, 1.0F);
                        }
                    }
                }

                if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
                    if(UHCAPI.getInstance().getModuleHandler().hasModule() && UHCAPI.getInstance().getModuleHandler().getModule().hasRoles()) {
                        if(!UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().isEmpty()) {
                            List<Camp> camps = getCamps();

                            if(camps.size() != 1) return;

                            Camp camp = camps.get(0);

                            if(camp.isSolo() && UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() > 1) return;

                            Bukkit.broadcastMessage(ChatUtils.LINE + "");
                            Bukkit.broadcastMessage("");
                            if(camp.isSolo()) {
                                UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().get(0);
                                Bukkit.broadcastMessage("Le joueur " + camp.getPrefix() + uhcPlayer.getName() + " a gagne !");
                            }
                            else {
                                Bukkit.broadcastMessage("Les " + camp.getPrefix() + camp.getName() + "§r ont gagnes !");
                            }
                            Bukkit.broadcastMessage("");
                            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersConnected()) {
                                if(uhcPlayer.getRole() != null && (
                                        uhcPlayer.getPlayerState() == PlayerState.IN_GAME ||
                                                uhcPlayer.getPlayerState() == PlayerState.DEAD
                                )) {
                                    Bukkit.broadcastMessage(uhcPlayer.getName() + " (" + uhcPlayer.getRole().getStartCamp().getPrefix() + uhcPlayer.getRole().getName() + "§r) : " + uhcPlayer.getKilledPlayers().size() + " kill(s)");
                                }
                            }
                            Bukkit.broadcastMessage("");
                            Bukkit.broadcastMessage(ChatUtils.LINE + "");

                            UHCAPI.getInstance().getGameHandler().setGameState(GameState.ENDING);
                        }
                    }
                    else if(UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() < 2) {
                        UHCAPI.getInstance().getGameHandler().setGameState(GameState.ENDING);
                        UHCPlayer winner = UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().get(0);

                        Bukkit.broadcastMessage(ChatUtils.LINE + "");
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage("Le joueur " + winner.getName() + " a gagne !");
                        Bukkit.broadcastMessage("");
                        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersConnected()) {
                            if(uhcPlayer.getRole() != null && (
                                    uhcPlayer.getPlayerState() == PlayerState.IN_GAME ||
                                            uhcPlayer.getPlayerState() == PlayerState.DEAD
                            )) {
                                Bukkit.broadcastMessage(uhcPlayer.getName() + " : " + uhcPlayer.getKilledPlayers().size() + " kill(s)");
                            }
                        }
                        Bukkit.broadcastMessage("");
                        Bukkit.broadcastMessage(ChatUtils.LINE + "");
                    }
                }
            }
            else {
                uhcVictim.revive();
            }
        }, 5 * 20L);
    }

    private static List<Camp> getCamps() {
        List<Camp> camps = new ArrayList<>();

        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(uhcPlayer.getPlayer() == null || uhcPlayer.getRole() == null) continue;
            Camp camp = uhcPlayer.getRole().getCamp();
            if(!camps.contains(camp)) {
                camps.add(camp);
            }
        }
        return camps;
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

        String message = getMessage(event);
        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInSpec()) {
            uhcPlayer.sendMessage(message);
        }
    }

    private static String getMessage(UHCPlayerDeathEvent event) {
        String message;
        if(event.getKiller() != null) {
            message = ChatUtils.GLOBAL_INFO.getMessage(event.getKiller().getName() + " a tue " + event.getVictim().getName() + ".");
        }
        else if(event.getVictim().getPlayer() != null) {
            message = ChatUtils.GLOBAL_INFO.getMessage(event.getVictim().getPlayer().getName() + " est mort par " + event.getVictim().getPlayer().getLastDamageCause().getCause().name() + ".");
        }
        else {
            message = ChatUtils.GLOBAL_INFO.getMessage(event.getVictim().getPlayer().getName() + " est mort.");
        }
        return message;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING ||
            UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onRegainHeal(EntityRegainHealthEvent event) {
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING) {
            event.setCancelled(true);
        }
    }
}
