package me.butter.ninjago.commands.list;

import me.butter.api.UHCAPI;
import me.butter.api.module.roles.RoleType;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.timer.list.RoleTimer;
import me.butter.ninjago.NinjagoV2;
import me.butter.ninjago.roles.CampEnum;

import java.util.HashMap;
import java.util.Map;

public class CompoCommand extends AbstractCommand {
    public CompoCommand() {
        super("compo", "roles");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        sender.sendMessage(ChatUtils.LINE.prefix);
        sender.sendMessage("");
        for(CampEnum campEnum : CampEnum.values()) {
            if(campEnum.isHidden()) continue;

            int rolesAmount = 0;
            Map<String, Integer> roles = new HashMap<>();

            if(UHCAPI.getInstance().getTimerHandler().getTimer(RoleTimer.class).isFired()) {
                for (UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                    if(uhcPlayer.getRole() != null && uhcPlayer.getRole().getCamp() == campEnum.getCamp()) {
                        if(!roles.containsKey(uhcPlayer.getRole().getName())) {
                            roles.put(uhcPlayer.getRole().getName(), 1);
                        } else {
                            roles.put(uhcPlayer.getRole().getName(), roles.get(uhcPlayer.getRole().getName()) + 1);
                        }
                        rolesAmount++;
                    }
                }
            }
            else {
                for (RoleType roleType : NinjagoV2.getInstance().getRolesComposition()) {
                    if(roleType.getAmount() > 0 && roleType.getCamp() == campEnum.getCamp()) {
                        roles.put(roleType.getName(), roleType.getAmount());
                        rolesAmount += roleType.getAmount();
                    }
                }
            }

            sender.sendMessage(ChatUtils.LIST_HEADER.getMessage(campEnum.getCamp().getPrefix() + campEnum.getCamp().getName() + "§r (" + rolesAmount + ")"));
            for(Map.Entry<String, Integer> role : roles.entrySet()) {
                sender.sendMessage(ChatUtils.LIST_ELEMENT.getMessage(role.getKey() + " (" + role.getValue() + ")"));
            }
            sender.sendMessage("");
        }
        sender.sendMessage(ChatUtils.LINE.prefix);
    }
}
