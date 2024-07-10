package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Nya extends NinjagoRole {

    private int samuraiTimer = 0, maxSamuraiTimer = 2 * 60;
    private final int maxBookTimer = 5 * 60;

    private boolean bookGiven = false, samuraiActive = false;

    private UHCPlayer finalKay;

    public Nya() {
        super("Nya", "/roles/ninjas-14/nya");
        addPower(new SamuraiPower());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Au 3eme épisode, vous obtenez une liste de §l3 pseudos§r contenant celui de §aKai§r.",
                "En restant 5 minutes à 10 blocs de celui-ci, vous obtiendrez un livre §9Depth Strider 3§r."
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public void onDistributionFinished() {
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Kai && role.getUHCPlayer() != null) finalKay = role.getUHCPlayer();
        }

        if(finalKay == null) return;
        Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), new Runnable() {
            int bookTimer = 0;

            @Override
            public void run() {
                if(getUHCPlayer() == null || finalKay == null) {
                    return;
                }

                if(getUHCPlayer().isNextTo(finalKay, 10)) {
                    if(!bookGiven) {
                        bookTimer++;
                        if(bookTimer >= maxBookTimer) {
                            DepthStriderBook book = new DepthStriderBook();
                            addPower(book);
                            getUHCPlayer().giveItem(book.getItem(), true);
                            bookGiven = true;

                            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu un livre Depth Strider 3 !"));
                        }
                    }
                }
            }
        }, 20, 20);
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        if(event.getEpisode() == 3 && finalKay != null) {
            List<UHCPlayer> alivePlayers = new ArrayList<>(UHCAPI.getInstance().getPlayerHandler().getPlayersInGame());
            alivePlayers.removeIf(u -> u.getRole() instanceof Kai || u.equals(getUHCPlayer()));

            Collections.shuffle(alivePlayers);
            List<UHCPlayer> playersList = alivePlayers.subList(0, 2);
            playersList.add(finalKay);
            Collections.shuffle(playersList);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                    "Le pseudo du joueur Kai est parmis les suivants :"
            ));
            for(UHCPlayer uhcPlayer : playersList) {
                getUHCPlayer().sendMessage(ChatUtils.LIST_ELEMENT.getMessage(
                        "§e" + uhcPlayer.getPlayer().getName()
                ));
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(UHCPlayerDeathEvent event) {
        if(event.getKiller() != getUHCPlayer()) return;

        maxSamuraiTimer += 30;
        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez gagné 30 secondes d'utilisation supplémentaires de votre samurai X !"));
    }

    private class SamuraiPower extends ItemPower {

        SamuraiTimer timer;

        public SamuraiPower() {
            super("§3Samurai X", Material.NETHER_STAR, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Lorsque cet item est activé, vous obtenez §9Speed 1§r et §cForce 1§r.",
                    "Vous avez §l2 minutes§r d'utilisation.",
                    "Lorsque vous faites un kill, vous obtenez §l30 secondes d'utilisation§r supplémentaires.",
                    "Vous pouvez vérifier le temps qu'il vous reste avec un clic gauche."
            };
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(timer == null) {
                timer = new SamuraiTimer(player);
                timer.runTaskTimer(Ninjago.getInstance(), 0, 20);
            }

            if(clickAction == Action.RIGHT_CLICK_AIR || clickAction == Action.RIGHT_CLICK_BLOCK) {
                if(samuraiTimer > maxSamuraiTimer) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez plus assez de temps !"));
                }
                else {
                    samuraiActive = !samuraiActive;
                    if(samuraiActive) {
                        player.addStrength(15);
                        player.addSpeed(20);
                        player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Samurai X activé"));
                    }
                    else {
                        player.removeStrength(15);
                        player.removeSpeed(20);
                        player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Samurai X désactivé"));
                    }
                }
            }
            else {
                if(samuraiTimer > maxSamuraiTimer) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez plus assez de temps !"));
                }
                else {
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Il vous reste " + GraphicUtils.convertToAccurateTime(maxSamuraiTimer - samuraiTimer) + " d'utilisation"));
                }
            }
            return false;
        }

        private class SamuraiTimer extends BukkitRunnable {
            UHCPlayer player;

            public SamuraiTimer(UHCPlayer player) {
                this.player = player;
            }

            @Override
            public void run() {
                if(samuraiActive) {
                    samuraiTimer++;
                    if(samuraiTimer > maxSamuraiTimer) {
                        samuraiActive = false;
                        player.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez plus assez de temps, votre samurai X s'est désactivé"));
                    }
                }
            }
        }
    }

    public static class DepthStriderBook extends EnchantBookPower {
        public DepthStriderBook() {
            super("§9Livre Depth Strider 3", Enchantment.DEPTH_STRIDER, 3);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Un livre enchanté Depth Strider 3. Il est possible de le fusionner avec une piece en diamant."
            };
        }

        @Override
        public boolean showPower() {
            return false;
        }
    }
}
