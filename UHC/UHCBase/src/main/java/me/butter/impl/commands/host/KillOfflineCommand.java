package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

import java.util.List;
import java.util.stream.Collectors;

public class KillOfflineCommand extends AbstractCommand {
    public KillOfflineCommand() {
        super("killoffline");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 2) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h killoffline <joueur>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        if(!target.isDisconnected()) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + target.getName() + " n'est pas deconnecté !"));
            return;
        }

        target.setPlayerState(PlayerState.DEAD);
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        return UHCAPI.getInstance().getPlayerHandler().getAllPlayers().stream()
                .filter(UHCPlayer::isDisconnected)
                .map(UHCPlayer::getName).collect(Collectors.toList());
    }
}
