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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Nya extends NinjagoRole {

    static boolean samuraiActive = false;
    static int samuraiTimer = 0;
    static int maxSamuraiTimer = 2 * 60;

    static int maxBookTimer = 5 * 60;

    public Nya() {
        super("Nya", "doc", Arrays.asList(
                new SamuraiPower()
        ));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Au 3eme épisode, vous obtenez une liste de 3 pseudos contenant celui de Kai.",
                "En restant 10 minutes à 10 blocs de celui-ci, vous obtiendrez un livre depth rider 3."
        };
    }

    @Override
    public void onDistributionFinished() {
        UHCPlayer kay = null;
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Nya && role.getUHCPlayer() != null) {
                kay = role.getUHCPlayer();
            }
        }
        UHCPlayer finalKay = kay;
        Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), new Runnable() {
            boolean nextToKay = false;
            int bookTimer = 0;

            @Override
            public void run() {
                if(getUHCPlayer() == null) {
                    return;
                }

                samuraiTimer++;
                if(samuraiActive && samuraiTimer > maxSamuraiTimer) {
                    samuraiActive = false;
                    getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez plus assez de temps, votre samurai X s'est désactivé"));
                }

                if(finalKay == null) return;
                if(finalKay.getLocation().distance(getUHCPlayer().getLocation()) <= 10 && !nextToKay) {
                    nextToKay = true;
                }
                else if(finalKay.getLocation().distance(getUHCPlayer().getLocation()) > 10 && nextToKay) {
                    nextToKay = false;
                    bookTimer = 0;
                }

                if(nextToKay) {
                    bookTimer++;
                    if(bookTimer >= maxBookTimer) {
                        DepthStriderBook book = new DepthStriderBook();
                        addPower(book);
                        getUHCPlayer().giveItem(book.getItem(), true);
                    }
                }
            }
        }, 20, 20);
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        if(event.getEpisode() == 3) {
            List<UHCPlayer> alivePlayers = new ArrayList<>(UHCAPI.getInstance().getPlayerHandler().getPlayersInGame());
            UHCPlayer kay = alivePlayers.stream().filter(u -> u.getRole() instanceof Kai).findFirst().orElse(null);
            alivePlayers.removeIf(u -> u.getRole() instanceof Kai);

            Collections.shuffle(alivePlayers);
            List<UHCPlayer> playersList = alivePlayers.subList(0, 2);
            playersList.add(kay);
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
    }

    private static class DepthStriderBook extends EnchantBookPower {
        public DepthStriderBook() {
            super("§rLivre Depth Strider 3", Enchantment.DEPTH_STRIDER, 3);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Un livre enchanté Depth Strider 3. Il est possible de le fusionner avec une piece en diamant."};
        }
    }

    private static class SamuraiPower extends ItemPower {
        public SamuraiPower() {
            super("§3Samurai X", Material.NETHER_STAR, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Lorsque cet item est activé, vous obtenez 20% de speed et 20% de force. ",
                    "Vous avez 2 minutes d'utilisation. ",
                    "Lorsque vous faites un kill, vous obtenez 30 secondes d'utilisation supplémentaires",
                    "Vous pouvez vérifier le temps qu'il vous reste avec un clic gauche"
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(clickAction == Action.RIGHT_CLICK_AIR || clickAction == Action.RIGHT_CLICK_BLOCK) {
                if(samuraiTimer > maxSamuraiTimer) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez plus assez de temps !"));
                }
                else {
                    samuraiActive = !samuraiActive;
                    if(samuraiActive) {
                        player.addStrength(20);
                        player.addSpeed(20);
                        player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Samurai X activé"));
                    }
                    else {
                        player.removeStrength(20);
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
                    player.sendMessage(ChatUtils.ERROR.getMessage("Il vous reste " + GraphicUtils.convertToAccurateTime(maxSamuraiTimer - samuraiTimer) + " d'utilisation"));
                }
            }
            return false;
        }
    }
}
