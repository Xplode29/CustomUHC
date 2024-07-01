package me.butter.impl.commands;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandDoc implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return true;

        if(UHCAPI.getInstance().getModuleHandler().hasModule()) {
            String url = UHCAPI.getInstance().getModuleHandler().getModule().getDoc();
            if(uhcPlayer.getRole() != null) url += uhcPlayer.getRole().getDoc();
            uhcPlayer.sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Documentation: " + url));
        }
        else {
            uhcPlayer.sendMessage(ChatUtils.ERROR.getMessage("Ce mode de jeu n'a pas de documentation !"));
        }
        return true;
    }
}
