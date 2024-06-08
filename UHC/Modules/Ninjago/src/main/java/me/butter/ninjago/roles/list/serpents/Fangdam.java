package me.butter.ninjago.roles.list.serpents;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fangdam extends NinjagoRole {

    UHCPlayer pythor, fangtom;

    boolean nextToDuo;

    FangChat fangChat;

    public Fangdam() {
        super("Fangdam", "/roles/serpent/fangdam", Collections.singletonList(new FangChat()));
        for(Power power : getPowers()) {
            if(power instanceof FangChat) {
                fangChat = (FangChat) power;
                break;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 a 20 blocks de Fangtom. ",
                "A l'annonce des roles, vous obtenez le pseudo de Pythor et de Fangtom."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Arrays.asList(
                ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de Pythor" : "Pythor:" + pythor.getName()),
                ChatUtils.PLAYER_INFO.getMessage(fangtom == null ? "Pas de Fangtom" : "Fangtom:" + fangtom.getName())
        );
    }

    @Override
    public void onDistributionFinished() {
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role.getUHCPlayer() == null) continue;
            if(role instanceof Fangtom) fangtom = role.getUHCPlayer();
            if(role instanceof Pythor) pythor = role.getUHCPlayer();
        }

        if (fangtom != null) {
            fangChat.fangtom = fangtom;
            Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
                if(getUHCPlayer() == null) {
                    return;
                }

                if(getUHCPlayer().isNextTo(fangtom, 20) && !nextToDuo) {
                    getUHCPlayer().addStrength(20);
                    nextToDuo = true;
                }
                else if(!getUHCPlayer().isNextTo(fangtom, 20) && nextToDuo) {
                    getUHCPlayer().removeStrength(20);
                    nextToDuo = false;
                }
            }, 20, 20);
        }
    }

    @Override
    public boolean isInList() {
        return true;
    }

    private static class FangChat extends CommandPower {

        UHCPlayer fangtom;

        public FangChat() {
            super("Chat avec Fangtom", "fchat", 0, -1);
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Vous permet de communiquer avec Fangtom"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            List<String> message = Lists.newArrayList(args);
            message.remove("fchat");
            if(fangtom != null) {
                fangtom.sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                        "Fangdam: " + Joiner.on(" ").join(message)
                ));
            }
            else {
                player.sendMessage(ChatUtils.ERROR.getMessage("Il n'y a pas de Fangtom dans cette partie !"));
            }
            return false;
        }
    }
}
