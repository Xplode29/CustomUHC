package me.butter.ninjago.commands;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.api.utils.chat.ChatSnippets;
import me.butter.impl.timer.list.RoleTimer;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.RoleEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandNinjago implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) commandSender);
        if(sender == null) return false;

        if(strings.length == 0) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Usage: /ni <argument>"));
            return true;
        }

        switch (strings[0]) {
            case "role":
                if(sender.getRole() == null) {
                    sender.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez pas de role !"));
                    return true;
                }

                ChatSnippets.rolePresentation(sender);
                break;

            case "effects":
                ChatSnippets.listEffects(sender);
                break;

            case "roles":
                sender.sendMessage(ChatUtils.SEPARATOR.prefix);
                sender.sendMessage("");
                for(CampEnum campEnum : CampEnum.values()) {
                    int rolesAmount = 0;
                    List<String> roles = new ArrayList<>();

                    for (RoleEnum roleEnum : RoleEnum.values()) {
                        if (roleEnum.getCampEnum() == campEnum) {
                            int amount = Ninjago.getInstance().getRolesComposition().get(roleEnum.getRoleClass());
                            if (amount > 0) {
                                rolesAmount += amount;
                                roles.add(ChatUtils.LIST_ELEMENT.getMessage(roleEnum.getName() + " (" + amount + ")"));
                            }
                        }
                    }

                    sender.sendMessage(ChatUtils.LIST_HEADER.getMessage(campEnum.getCamp().getName() + "§r (" + rolesAmount + ")"));
                    for(String role : roles) {
                        sender.sendMessage(role);
                    }
                    sender.sendMessage("");
                }
                sender.sendMessage(ChatUtils.SEPARATOR.prefix);
                break;

            default:
                if(sender.getRole() != null) {
                    for(Power power : sender.getRole().getPowers()) {
                        if(power instanceof CommandPower) {
                            if(strings[0].equalsIgnoreCase(((CommandPower) power).getArgument())) {
                                ((CommandPower) power).onUsePower(sender, strings);
                                return true;
                            }
                        }
                    }
                }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return Collections.emptyList();
    }
}
