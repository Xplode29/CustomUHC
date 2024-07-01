package me.butter.ninjago.roles.list.serpents;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;

public class Fangdam extends NinjagoRole {

    private UHCPlayer pythor, fangtom;

    private boolean nextToDuo;

    public Fangdam() {
        super("Fangdam", "/roles/serpent/fangdam");
        addPower(new FangChat());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez §cForce 1§r à 20 blocks de §1Fangtom§r. ",
                "A l'annonce des roles, vous obtenez le pseudo de §1Pythor§r et de §1Fangtom§r."
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
            Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
                if(fangtom.getPlayerState() == PlayerState.DEAD && !nextToDuo) return;
                if(getUHCPlayer().getPlayer() == null || fangtom.getPlayer() == null) {
                    return;
                }

                if(fangtom.getPlayerState() == PlayerState.DEAD) {
                    if(nextToDuo) {
                        getUHCPlayer().removeStrength(15);
                        nextToDuo = false;
                    }
                    return;
                }

                if(getUHCPlayer().isNextTo(fangtom, 20) && !nextToDuo) {
                    getUHCPlayer().addStrength(15);
                    nextToDuo = true;
                }
                else if(!getUHCPlayer().isNextTo(fangtom, 20) && nextToDuo) {
                    getUHCPlayer().removeStrength(15);
                    nextToDuo = false;
                }
            }, 20, 20);
        }
    }

    private class FangChat extends CommandPower {

        public FangChat() {
            super("Chat avec §1Fangtom§r", "chat", 0, -1);
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Vous permet de communiquer avec §1Fangtom§r"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            List<String> message = Lists.newArrayList(args);
            message.remove(0);
            if(fangtom != null) {
                fangtom.sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                        "Fangdam: " + Joiner.on(" ").join(message)
                ));
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage(
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
