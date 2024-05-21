package me.butter.ninjago.commands;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ChatUtils;
import me.butter.ninjago.chat.ChatModule;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

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

                ChatModule.rolePresentation(sender);

                break;
            case "effects":
                ChatModule.listEffects(sender);
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
