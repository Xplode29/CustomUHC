package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.event.EventHandler;

import java.util.Collections;
import java.util.List;

public class Slithraa extends NinjagoRole {

    boolean hadSpeed = false;

    UHCPlayer skales;

    public Slithraa() {
        super("Slithraa", "/roles/serpent/bytar", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A l'annonce des roles, vous obtenez le pseudo de Skales.",
                "Lorsque vous effectuez un kill, vous obtenez 3% de force supplémentaire",
                "Si Skales meurt sans avoir infecté un joueur, vous obtenez 20% de speed supplémentaire"
        };
    }

    @Override
    public List<String> additionalDescription() {
        if(skales == null || skales.getPlayerState() != PlayerState.IN_GAME) {
            for (Role role : Ninjago.getInstance().getRolesList()) {
                if(role instanceof Skales && role.getUHCPlayer() != null && role.getUHCPlayer().getPlayerState() == PlayerState.IN_GAME) {
                    skales = role.getUHCPlayer();
                    break;
                }
            }
        }

        if(skales == null) return Collections.emptyList();
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage("Skales:" + skales.getName()));
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller() != null && event.getKiller().equals(getUHCPlayer())) {
            getUHCPlayer().addStrength(3);
        }

        if(event.getVictim().getRole() instanceof Skales && !hadSpeed) {
            if(!((Skales) event.getVictim().getRole()).infectionPower.infected) {
                getUHCPlayer().addSpeed(20);
                hadSpeed = true;
            }
        }
    }
}
