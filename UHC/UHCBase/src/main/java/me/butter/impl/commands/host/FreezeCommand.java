package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

import java.util.List;
import java.util.stream.Collectors;

public class FreezeCommand extends AbstractCommand {
    public FreezeCommand() {
        super("freeze");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 2) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h freeze <joueur>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null || target.getPlayer() == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        target.setAbleToMove(!target.isAbleToMove());

        sender.getPlayer().sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Vous avez " + (target.isAbleToMove() ? "unfreeze " : "freeze ") + target.getName()));
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        return UHCAPI.getInstance().getPlayerHandler().getPlayersConnected().stream()
                .filter(player -> player.getPlayer() != null)
                .map(UHCPlayer::getName).collect(Collectors.toList());
    }
}
