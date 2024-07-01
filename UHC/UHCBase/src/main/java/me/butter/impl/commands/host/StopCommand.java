package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

public class StopCommand extends AbstractCommand {
    public StopCommand() {
        super("stop");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(!UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("La partie n'a pas commence !"));
        }
        UHCAPI.getInstance().reset();
    }
}
