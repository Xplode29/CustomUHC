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

    private boolean hadEffects = false;
    private UHCPlayer skales;

    public Slithraa() {
        super("Slithraa", "/roles/serpent-10/slithraa");
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A l'annonce des roles, vous obtenez le pseudo de §1Skales§r.",
                "Lorsque vous effectuez un kill, vous obtenez §c3% de force§r supplémentaire",
                "Si Skales meurt, vous obtenez §9Speed 1§r permanent."
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
                        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu §9Speed 1§r suite a la mort de §1Skales§r."));
                        hadEffects = true;
                    }
                }
            }, 0, 20);
        }
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(skales == null ? "Il n'y a pas de §1Skales§r dans cet partie" : "§1Skales§r:" + skales.getName()));
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller() != null && event.getKiller().equals(getUHCPlayer())) {
            getUHCPlayer().addStrength(3);
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu §c3% de force§r."));
        }
    }
}
