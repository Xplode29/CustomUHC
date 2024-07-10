package me.butter.impl.commands.debug;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.timer.Timer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListPlayersCommand extends AbstractCommand {

    public ListPlayersCommand() {
        super("listplayers");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getAllPlayers()) {
            sender.sendMessage(uhcPlayer.getName() + (uhcPlayer.isDisconnected() ? " (disconnected)" : ""));
        }
    }
}
