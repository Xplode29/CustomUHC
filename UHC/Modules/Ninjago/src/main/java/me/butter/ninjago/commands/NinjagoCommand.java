package me.butter.ninjago.commands;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.ninjago.commands.list.*;
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

public class NinjagoCommand implements TabExecutor {
    private final List<AbstractCommand> commands = new ArrayList<>();

    public NinjagoCommand() {
        commands.add(new CommandEffects());
        commands.add(new CommandRole());
        commands.add(new CommandCompo());
        commands.add(new CommandDoc());
        commands.add(new CommandFull());
        commands.add(new CommandRules());;
        commands.add(new CommandColor());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(((Player) commandSender).getUniqueId());
        if(sender == null) return true;

        if(strings.length == 0) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Usage: /n <argument>"));
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
                    if(sender.getPlayerState() == PlayerState.IN_GAME) ((CommandPower) power).onUsePower(sender, strings);
                    else sender.sendMessage(ChatUtils.ERROR.getMessage("Vous devez etre en jeu pour utiliser ce pouvoir !"));
                    return true;
                }
            }
        }

        sender.sendMessage(ChatUtils.ERROR.getMessage("Usage: /n <" + Strings.join(commands.stream().map(AbstractCommand::getArgument).collect(Collectors.toList()), " | ") + ">"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return Collections.emptyList();
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(((Player) commandSender).getUniqueId());
        if(sender == null) return Collections.emptyList();

        String typing = strings.length > 0 ? strings[strings.length - 1] : "";

        if(strings.length < 2) {
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

            return arguments.stream().filter(argument -> argument.startsWith(typing)).collect(Collectors.toList());
        }
        else {
            for(AbstractCommand command : commands) {
                if(command.getArgument().equalsIgnoreCase(strings[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                    return command.onTabComplete(sender, s, strings).stream().filter(argument -> argument.startsWith(typing)).collect(Collectors.toList());
                }
            }

            if(strings.length == 2 && sender.getRole() != null && sender.getPlayerState()== PlayerState.IN_GAME) {
                for(Power power : sender.getRole().getPowers()) {
                    if(power instanceof TargetCommandPower) {
                        if(strings[0].equalsIgnoreCase(((TargetCommandPower) power).getArgument())) {
                            return UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().stream().filter(uhcPlayer -> uhcPlayer.getPlayer() != null).map(UHCPlayer::getName).filter(argument -> argument.startsWith(typing)).collect(Collectors.toList());
                        }
                    }
                }
            }

            return Collections.emptyList();
        }
    }
}
