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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

public class Bytar extends NinjagoRole {

    private UHCPlayer skalidor;

    public Bytar() {
        super("Bytar", "/roles/serpent/bytar", new DiggingPower());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A l'annonce des roles, vous obtenez le pseudo de §1Skalidor§r.",
                "Lorsque vous effectuez un kill, vous obtenez §73% de Résistance§r supplémentaires.",
                "Au debut du jour, vous etes ralenti pendant 30 secondes."
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
        getUHCPlayer().addPotionEffect(PotionEffectType.SLOW, 5, 1);
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller() != null && event.getKiller().equals(getUHCPlayer())) {
            getUHCPlayer().addResi(3);
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous gagnez 3% de resistance."));
        }
    }

    private static class DiggingPower extends TargetPlayerItemPower {

        public DiggingPower() {
            super("Creusage", Material.NETHER_STAR, 20, 15 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Vous passez en mode Spectateur pendant 3 secondes puis etes téléporté derriere le joueur visé."
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

                    Location loc = target.getPlayer().getLocation().clone().add(target.getPlayer().getLocation().getDirection().setY(0).normalize().multiply(-2));
                    loc.getBlock().setType(Material.AIR);
                    loc.clone().add(0, 1, 0).getBlock().setType(Material.AIR);

                    player.getPlayer().teleport(loc);

                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ete téléporté vers " + target.getName()));
                }
            }.runTaskLater(UHCAPI.getInstance(), 3 * 20);

            return true;
        }
    }
}
