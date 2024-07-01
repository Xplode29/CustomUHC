package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.timer.Timer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ForceTimerCommand extends AbstractCommand {

    public ForceTimerCommand() {
        super("force");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Usage: /" + command + " <timer>"));
            return;
        }
        if (!UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("La partie n'a pas commence."));
            return;
        }

        Timer timer = UHCAPI.getInstance().getTimerHandler().getTimer(args[1]);
        if(timer == null) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Le timer '" + args[1] + "' n'existe pas."));
            return;
        }

        if(timer.isFired()) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Le timer '" + args[1] + "' est déja actif."));
            return;
        }

        timer.fireTimer();
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length > 0) {
            return UHCAPI.getInstance().getTimerHandler().getTimers().stream().filter(timer -> !timer.isFired()).map(Timer::getName).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
