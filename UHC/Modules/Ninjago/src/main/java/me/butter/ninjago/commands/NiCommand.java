package me.butter.ninjago.commands;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.ninjago.commands.list.CompoCommand;
import me.butter.ninjago.commands.list.ListEffectsCommand;
import me.butter.ninjago.commands.list.RoleCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class NiCommand implements TabExecutor {
    private final List<AbstractCommand> commands = new ArrayList<>();

    public NiCommand() {
        commands.add(new ListEffectsCommand());
        commands.add(new RoleCommand());
        commands.add(new CompoCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(((Player) commandSender).getUniqueId());
        if(sender == null) return true;

        if(strings.length == 0) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Usage: /ni <argument>"));
            return true;
        }

        for(AbstractCommand command : commands) {
            if(command.getArgument().equalsIgnoreCase(strings[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                command.onCommand(sender, s, strings);
                return true;
            }
        }

        if(sender.getRole() == null) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez pas de role !"));
            return true;
        }
        for(Power power : sender.getRole().getPowers()) {
            if(power instanceof CommandPower) {
                if(strings[0].equalsIgnoreCase(((CommandPower) power).getArgument())) {
                    ((CommandPower) power).onUsePower(sender, strings);
                    return true;
                }
            }
        }

        sender.sendMessage(ChatUtils.ERROR.getMessage("Usage: /ni <" + Strings.join(commands.stream().map(AbstractCommand::getArgument).collect(Collectors.toList()), " | ") + ">"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return Collections.emptyList();
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(((Player) commandSender).getUniqueId());
        if(sender == null) return Collections.emptyList();

        if(strings.length == 1) {
            List<String> arguments = new ArrayList<>();

            if(sender.getRole() != null) {
                for(Power power : sender.getRole().getPowers()) {
                    if(power instanceof CommandPower) {
                        arguments.add(((CommandPower) power).getArgument());
                    }
                }
            }

            for(AbstractCommand command : commands) {
                arguments.add(command.getArgument());
            }
            return arguments;
        }
        else if (strings.length == 2) {
            for(AbstractCommand command : commands) {
                if(command.getArgument().equalsIgnoreCase(strings[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                    return command.onTabComplete(sender, s, strings);
                }
            }

            if(sender.getRole() == null) return Collections.emptyList();
            for(Power power : sender.getRole().getPowers()) {
                if(power instanceof TargetCommandPower) {
                    if(strings[0].equalsIgnoreCase(((TargetCommandPower) power).getArgument())) {
                        return UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().stream().map(UHCPlayer::getName).collect(Collectors.toList());
                    }
                }
            }
        }
        return Collections.emptyList();
    }
}
