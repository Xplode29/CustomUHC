package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;

import java.util.Collections;

public class Ed extends NinjagoRole {

    boolean nextToJay;

    public Ed() {
        super("Ed", "doc", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous apparaissez dans la liste de Pythor. ",
                "A l'annonce des roles, vous obtenez le pseudo de Jay. ",
                "Vous obtenez force à 15 blocks de celui-ci"
        };
    }

    @Override
    public boolean isInList() {
        return true;
    }

    @Override
    public void onDistributionFinished() {
        UHCPlayer jay = null;
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Jay && role.getUHCPlayer() != null) {
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Jay:" + role.getUHCPlayer().getName()));
                jay = role.getUHCPlayer();
            }
        }
        if (jay == null) {
            getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Il n'y a pas de Jay dans cette partie"));
        }
        else {
            UHCPlayer finalJay = jay;
            Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
                if(getUHCPlayer() == null) {
                    return;
                }

                if(finalJay.getLocation().distance(getUHCPlayer().getLocation()) <= 15 && !nextToJay) {
                    getUHCPlayer().addStrength(10);
                    nextToJay = true;
                }
                else if(finalJay.getLocation().distance(getUHCPlayer().getLocation()) > 15 && nextToJay) {
                    getUHCPlayer().removeStrength(10);
                    nextToJay = false;
                }
            }, 20, 20);
        }
    }
}
