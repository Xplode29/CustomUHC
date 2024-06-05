package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Bytar extends NinjagoRole {

    UHCPlayer skalidor;

    public Bytar() {
        super("Bytar", "/roles/serpent/bytar", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 la nuit",
                "A l'annonce des roles, vous obtenez le pseudo de Skalidor.",
                "Lorsque vous effectuez un kill, vous obtenez 3% de résistance supplémentaire"
        };
    }

    @Override
    public List<String> additionalDescription() {
        if(skalidor == null || skalidor.getPlayerState() != PlayerState.IN_GAME) {
            for (Role role : Ninjago.getInstance().getRolesList()) {
                if(role instanceof Skalidor && role.getUHCPlayer() != null && role.getUHCPlayer().getPlayerState() == PlayerState.IN_GAME) {
                    skalidor = role.getUHCPlayer();
                    break;
                }
            }
        }

        if(skalidor == null) return Collections.emptyList();
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage("Skalidor:" + skalidor.getName()));
    }

    @Override
    public void onDay() {
        getUHCPlayer().removeStrength(20);
        getUHCPlayer().addPotionEffect(PotionEffectType.SLOW, 30, 1);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addStrength(20);
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller().equals(getUHCPlayer())) {
            getUHCPlayer().addResi(3);
        }
    }
}
