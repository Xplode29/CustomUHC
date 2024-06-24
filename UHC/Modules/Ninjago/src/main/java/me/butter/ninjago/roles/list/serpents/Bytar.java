package me.butter.ninjago.roles.list.serpents;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.TargetPlayerItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

public class Bytar extends NinjagoRole {

    UHCPlayer skalidor;

    public Bytar() {
        super("Bytar", "/roles/serpent/bytar", new DiggingPower());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A l'annonce des roles, vous obtenez le pseudo de Skalidor.",
                "Lorsque vous effectuez un kill, vous obtenez 3% de résistance supplémentaire",
                "Au debut du jour, vous obtenez Lenteur 1 pendant 30 secondes."
        };
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Skalidor && role.getUHCPlayer() != null && role.getUHCPlayer().getPlayerState() == PlayerState.IN_GAME) {
                skalidor = role.getUHCPlayer();
            }
        }
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(skalidor == null ? "Il n'y a pas de Skalidor dans cet partie" : "Skalidor:" + skalidor.getName()));
    }

    @Override
    public void onDay() {
        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous etes ralenti pendant 30 secondes !"));
        getUHCPlayer().removeSpeed(20);
        Bukkit.getScheduler().runTaskLater(Ninjago.getInstance(), () -> getUHCPlayer().addSpeed(20), 30 * 20);
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller() != null && event.getKiller().equals(getUHCPlayer())) {
            getUHCPlayer().addResi(3);
        }
    }

    private static class DiggingPower extends TargetPlayerItemPower {
        public DiggingPower() {
            super("Creusage", Material.NETHER_STAR, 20, 15 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Vous passez en mode Spectateur pendant 3 secondes puis etes teleporte derriere le joueur vise."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, Action clickAction) {
            player.getPlayer().setGameMode(GameMode.SPECTATOR);
            player.setAbleToMove(false);
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous creusez un trou vers " + target.getName()));

            target.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous entendez un Constructor creuser un trou vers vous !"));

            new BukkitRunnable() {
                @Override
                public void run() {
                    if(player.getPlayer() == null) {
                        player.setAbleToMove(true);
                        cancel();
                        return;
                    }

                    player.getPlayer().setGameMode(GameMode.SURVIVAL);
                    player.setAbleToMove(true);

                    player.getPlayer().teleport(target.getPlayer().getLocation().clone().add(target.getPlayer().getLocation().getDirection().setY(0).normalize().multiply(-2)));
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ete teleporte vers " + target.getName()));
                }
            }.runTaskLater(UHCAPI.getInstance(), 3 * 20);

            return true;
        }
    }
}
