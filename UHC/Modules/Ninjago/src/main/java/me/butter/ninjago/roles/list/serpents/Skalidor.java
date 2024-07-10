package me.butter.ninjago.roles.list.serpents;

import me.butter.api.UHCAPI;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;

import java.util.Collections;
import java.util.List;

public class Skalidor extends NinjagoRole {

    private UHCPlayer pythor;

    public Skalidor() {
        super("Skalidor", "/roles/serpent-10/skalidor");
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez §7Resistance 1§r le jour et §cForce 1§r la nuit.",
                "A l'annonce des roles, vous obtenez le pseudo de §5Pythor§r."
        };
    }

    @Override
    public boolean isInList() {
        return true;
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
        if(UHCAPI.getInstance().getGameHandler().getGameConfig().isDay()) getUHCPlayer().addResi(20);
        else getUHCPlayer().addStrength(15);
    }

    @Override
    public void onDay() {
        getUHCPlayer().removeStrength(15);
        getUHCPlayer().addResi(20);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addStrength(15);
        getUHCPlayer().removeResi(20);
    }
}
