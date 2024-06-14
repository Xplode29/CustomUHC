package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.module.Module;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.UHCImpl;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.task.LaunchGameTask;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

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
