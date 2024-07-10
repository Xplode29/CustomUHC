package me.butter.impl.commands.debug;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DebugCommands implements TabExecutor {

    private final List<AbstractCommand> commands = new ArrayList<>();

    public DebugCommands() {
        commands.add(new GiveRoleCommand());
        commands.add(new ResetPowersCommand());
        commands.add(new ForceTimerCommand());
        commands.add(new ListPlayersCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(((Player) commandSender).getUniqueId());
        Player player = (Player) commandSender;
        if(sender == null) return true;

        if(strings.length > 0) {
            if(player.isOp()) {
                for(AbstractCommand command : commands) {
                    if(command.getArgument().equalsIgnoreCase(strings[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                        command.onCommand(sender, s, strings);
                        return true;
                    }
                }
            }
            else {
                player.sendMessage(ChatUtils.ERROR.getMessage("Tu n'as pas la permission !"));
                return true;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return Collections.emptyList();
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(((Player) commandSender).getUniqueId());
        if(sender == null) return Collections.emptyList();

        String typing = strings.length > 0 ? strings[strings.length - 1] : "";

        if(strings.length < 2) {
            return commands.stream().filter(command -> !command.isHidden()).map(AbstractCommand::getArgument).filter(argument -> argument.startsWith(typing)).collect(Collectors.toList());
        }
        else {
            for(AbstractCommand command : commands) {
                if(command.getArgument().equalsIgnoreCase(strings[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                    return command.onTabComplete(sender, s, strings).stream().filter(argument -> argument.startsWith(typing)).collect(Collectors.toList());
                }
            }
        }
        return Collections.emptyList();
    }
}
