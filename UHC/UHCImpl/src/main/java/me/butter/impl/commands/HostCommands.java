package me.butter.impl.commands;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.host.*;
import me.butter.impl.item.list.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HostCommands implements TabExecutor {

    private List<AbstractCommand> commands = new ArrayList<>();

    public HostCommands() {
        commands.add(new EffectCommand());
        commands.add(new ReviveCommand());
        commands.add(new SaveInvCommand());
        commands.add(new SetHostCommand());
        commands.add(new StartCommand());
        commands.add(new StopCommand());
        commands.add(new TeleportCommand());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command cmd, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(((Player) commandSender).getUniqueId());
        Player player = (Player) commandSender;
        if(sender == null) return true;

        if(strings.length == 0) {
            if(player.isOp()) {
                if(sender.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + sender.getName() + " est déja le host de la partie !"));
                    return true;
                }

                if(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() != null) {
                    UHCAPI.getInstance().getItemHandler().removeItemFromPlayer(MenuItem.class, UHCAPI.getInstance().getGameHandler().getGameConfig().getHost());
                }
                UHCAPI.getInstance().getItemHandler().giveItemToPlayer(MenuItem.class, sender);

                UHCAPI.getInstance().getGameHandler().getGameConfig().setHost(sender);

                Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(sender.getName() + " est maintenant le host de la partie."));
                return true;
            }
        }
        else {
            if(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null || !sender.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Tu n'es pas le host de la partie !"));
                return true;
            }
            for(AbstractCommand command : commands) {
                if(command.getArgument().equalsIgnoreCase(strings[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                    command.onCommand(sender, s, strings);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command cmd, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return Collections.emptyList();
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(((Player) commandSender).getUniqueId());
        Player player = (Player) commandSender;
        if(sender == null) return Collections.emptyList();

        if(strings.length < 2) {
            return commands.stream().map(AbstractCommand::getArgument).collect(Collectors.toList());
        }
        else {
            for(AbstractCommand command : commands) {
                if(command.getArgument().equalsIgnoreCase(strings[0]) || Arrays.stream(command.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(strings[0]))) {
                    return command.onTabComplete(sender, s, strings);
                }
            }
        }
        return Collections.emptyList();
    }

    public void addCommand(AbstractCommand command) {
        commands.add(command);
    }
}
