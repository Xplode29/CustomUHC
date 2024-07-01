package me.butter.ninjago.roles.list.serpents;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.NinjagoV2;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

public class Fangtom extends NinjagoRole {

    private UHCPlayer pythor, fangdam;

    private boolean nextToDuo;

    public Fangtom() {
        super("Fangtom", "/roles/serpent/fangtom");
        addPower(new FangChat());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez §7Résistance 1§r à 20 blocks de §1Fangtom§r. ",
                "A l'annonce des roles, vous obtenez le pseudo de §1Pythor§r et de §1Fangdam§r."
        };
    }

    @Override
    public boolean isInList() {
        return true;
    }

    @Override
    public List<String> additionalDescription() {
        return Arrays.asList(
                ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de Pythor" : "Pythor:" + pythor.getName()),
                ChatUtils.PLAYER_INFO.getMessage(fangdam == null ? "Pas de Fangdam" : "Fangdam:" + fangdam.getName())
        );
    }

    @Override
    public void onDistributionFinished() {
        for(Role role : NinjagoV2.getInstance().getRolesList()) {
            if(role.getUHCPlayer() == null) continue;
            if(role instanceof Fangdam) fangdam = role.getUHCPlayer();
            if(role instanceof Pythor) pythor = role.getUHCPlayer();
        }

        if (fangdam != null) {
            Bukkit.getScheduler().runTaskTimer(NinjagoV2.getInstance(), () -> {
                if(fangdam.getPlayerState() == PlayerState.DEAD && !nextToDuo) return;
                if(getUHCPlayer().getPlayer() == null || fangdam.getPlayer() == null) {
                    return;
                }

                if(fangdam.getPlayerState() == PlayerState.DEAD) {
                    if(nextToDuo) {
                        getUHCPlayer().removeResi(20);
                        nextToDuo = false;
                    }
                    return;
                }

                if(getUHCPlayer().isNextTo(this.fangdam, 20) && !nextToDuo) {
                    getUHCPlayer().addResi(20);
                    nextToDuo = true;
                }
                else if(!getUHCPlayer().isNextTo(this.fangdam, 20) && nextToDuo) {
                    getUHCPlayer().removeResi(20);
                    nextToDuo = false;
                }
            }, 20, 20);
        }
    }

    private class FangChat extends CommandPower {

        public FangChat() {
            super("Chat avec §1Fangdam", "chat", 0, -1);
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Vous permet de communiquer avec §1Fangdam§r."};
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            List<String> message = Lists.newArrayList(args);
            message.remove(0);
            if(fangdam != null) {
                fangdam.sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                        "Fangtom: " + Joiner.on(" ").join(message)
                ));
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                        "Fangtom: " + Joiner.on(" ").join(message)
                ));
            }
            else {
                player.sendMessage(ChatUtils.ERROR.getMessage("Il n'y a pas de Fangdam dans cette partie !"));
            }
            return false;
        }
    }
}
