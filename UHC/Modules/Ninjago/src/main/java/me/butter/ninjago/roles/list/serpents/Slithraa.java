package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;

import java.util.Collections;
import java.util.List;

public class Slithraa extends NinjagoRole {

    boolean hadEffects = false;

    UHCPlayer skales;

    public Slithraa() {
        super("Slithraa", "/roles/serpent/slithraa");
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A l'annonce des roles, vous obtenez le pseudo de Skales.",
                "Lorsque vous effectuez un kill, vous obtenez 3% de force supplémentaire",
                "Si Skales meurt, vous obtenez Speed 1 permanent."
        };
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Skales && role.getUHCPlayer() != null && role.getUHCPlayer().getPlayerState() == PlayerState.IN_GAME) {
                skales = role.getUHCPlayer();
                break;
            }
        }

        if(skales != null) {
            Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
                if(skales == null) return;

                if(skales.getPlayerState() != PlayerState.IN_GAME) {
                    if(!hadEffects) {
                        getUHCPlayer().addSpeed(20);
                        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu Speed 1 suite a la mort de Skales."));
                        hadEffects = true;
                    }
                }
            }, 0, 20);
        }
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(skales == null ? "Il n'y a pas de Skales dans cet partie" : "Skales:" + skales.getName()));
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller() != null && event.getKiller().equals(getUHCPlayer())) {
            getUHCPlayer().addStrength(3);
        }
    }
}
