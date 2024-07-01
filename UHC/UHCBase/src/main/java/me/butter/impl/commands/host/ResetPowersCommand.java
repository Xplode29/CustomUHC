package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.Power;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResetPowersCommand extends AbstractCommand {
    public ResetPowersCommand() {
        super("resetPowers");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 2) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h resetPowers <joueur>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        if(target.getRole() == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur n'a pas de role !"));
            return;
        }

        for(Power power : target.getRole().getPowers()) {
            power.reset();
        }
        target.getPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Les pouvoirs de " + target.getName() + " ont ete reset"));
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length == 2) {
            return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
