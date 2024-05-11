package me.butter.impl.commands;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ChatUtils;
import me.butter.impl.task.LaunchGameTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHost implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;
        Player player = (Player) commandSender;
        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);

        if(strings.length == 0) {
                if(player.isOp() && uhcPlayer != null) {
                    UHCAPI.get().getGameHandler().getGameConfig().setHost(uhcPlayer);
                    Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(uhcPlayer.getName() + " est maintenant le host de la partie."));
                    return true;
                }
        }

        else {
            if(
                    UHCAPI.get().getGameHandler().getGameConfig().getHost() == null ||
                    !uhcPlayer.equals(UHCAPI.get().getGameHandler().getGameConfig().getHost())
            ) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Tu n'es pas le host de la partie !"));
                return true;
            }

            switch (strings[0]) {
                case "stop":
                    if (UHCAPI.get().getGameHandler().getGameConfig().isStarting()) {
                        UHCAPI.get().getGameHandler().getGameConfig().setStarting(false);
                    }
                    return true;
                case "start":
                    if (!UHCAPI.get().getGameHandler().getGameConfig().isStarting()) {
                        UHCAPI.get().getGameHandler().getGameConfig().setStarting(true);
                        new LaunchGameTask();
                    }
                    return true;
                case "sethost":
                    if(strings.length < 2) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Usage : /h sethost <joueur>"));
                    }
                    UHCPlayer target = UHCAPI.get().getPlayerHandler().getUHCPlayer(strings[1]);

                    if(target == null) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + strings[1] + " n'existe pas !"));
                        return true;
                    }

                    UHCAPI.get().getGameHandler().getGameConfig().setHost(UHCAPI.get().getPlayerHandler().getUHCPlayer(strings[1]));
                    Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(target.getName() + " est maintenant le host de la partie."));
                    return true;
            }
        }

        return false;
    }
}
