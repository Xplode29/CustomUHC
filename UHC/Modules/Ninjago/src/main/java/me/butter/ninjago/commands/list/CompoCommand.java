package me.butter.ninjago.commands.list;

import me.butter.api.module.roles.RoleType;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;

import java.util.ArrayList;
import java.util.List;

public class CompoCommand extends AbstractCommand {
    public CompoCommand() {
        super("compo", "roles");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        sender.sendMessage(ChatUtils.SEPARATOR.prefix);
        sender.sendMessage("");
        for(CampEnum campEnum : CampEnum.values()) {
            int rolesAmount = 0;
            List<String> roles = new ArrayList<>();

            for (RoleType roleType : Ninjago.getInstance().getRolesComposition()) {
                if (roleType.getCamp() == campEnum.getCamp()) {
                    int amount = roleType.getAmount();
                    if (amount > 0) {
                        rolesAmount += amount;
                        roles.add(ChatUtils.LIST_ELEMENT.getMessage(roleType.getName() + " (" + amount + ")"));
                    }
                }
            }

            sender.sendMessage(ChatUtils.LIST_HEADER.getMessage(campEnum.getCamp().getPrefix() + campEnum.getCamp().getName() + "§r (" + rolesAmount + ")"));
            for(String role : roles) {
                sender.sendMessage(role);
            }
            sender.sendMessage("");
        }
        sender.sendMessage(ChatUtils.SEPARATOR.prefix);
    }
}
