package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;

import java.util.Collections;
import java.util.List;

public class Skalidor extends NinjagoRole {

    UHCPlayer pythor;

    public Skalidor() {
        super("Skalidor", "/roles/serpent/skalidor", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 la nuit ainsi que Resistance 1 permanent. ",
                "A l'annonce des roles, vous obtenez le pseudo de Pythor."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de Pythor" : "Pythor:" + pythor.getName()));
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Pythor && role.getUHCPlayer() != null) {
                pythor = role.getUHCPlayer();
                break;
            }
        }
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addResi(20);
    }

    @Override
    public boolean isInList() {
        return true;
    }

    @Override
    public void onDay() {
        getUHCPlayer().removeStrength(20);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addStrength(20);
    }
}
