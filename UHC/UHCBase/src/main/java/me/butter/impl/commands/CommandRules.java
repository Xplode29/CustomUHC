package me.butter.impl.commands;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.menu.list.player.RulesViewMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRules implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return true;

        uhcPlayer.openMenu(new RulesViewMenu(), false);

        return true;
    }
}
