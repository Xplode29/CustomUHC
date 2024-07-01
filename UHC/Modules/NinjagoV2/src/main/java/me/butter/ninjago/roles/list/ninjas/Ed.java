package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.NinjagoV2;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.List;

public class Ed extends NinjagoRole {

    UHCPlayer jay;
    boolean nextToJay;

    public Ed() {
        super("Ed", "/roles/ninjas/ed");
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous apparaissez dans la liste de §1Pythor§r.",
                "A l'annonce des roles, vous obtenez le pseudo de §aJay§r.",
                "Vous obtenez §cForce 1§r à 15 blocks de celui-ci."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(jay == null ? "Il n'y a pas de Jay dans cette partie" : "Jay : " + jay.getName());
    }

    @Override
    public boolean isInList() {
        return true;
    }

    @Override
    public void onDistributionFinished() {
        for(Role role : NinjagoV2.getInstance().getRolesList()) {
            if(role instanceof Jay && role.getUHCPlayer() != null) {
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Jay:" + role.getUHCPlayer().getName()));
                jay = role.getUHCPlayer();
            }
        }

        if (this.jay != null) {
            Bukkit.getScheduler().runTaskTimer(NinjagoV2.getInstance(), () -> {
                if(getUHCPlayer() == null) {
                    return;
                }

                if(jay.getPlayerState() != PlayerState.IN_GAME) {
                    if(nextToJay) {
                        getUHCPlayer().removeStrength(15);
                        nextToJay = false;
                    }
                    return;
                }

                if(getUHCPlayer().isNextTo(this.jay, 15) && !nextToJay) {
                    getUHCPlayer().addStrength(15);
                    nextToJay = true;
                }
                else if(!getUHCPlayer().isNextTo(this.jay, 15) && nextToJay) {
                    getUHCPlayer().removeStrength(15);
                    nextToJay = false;
                }
            }, 20, 20);
        }
    }
}
