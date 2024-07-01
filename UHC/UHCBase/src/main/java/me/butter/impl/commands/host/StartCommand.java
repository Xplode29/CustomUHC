package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.task.LaunchGameTask;

public class StartCommand extends AbstractCommand {

    public StartCommand() {
        super("start");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if (!UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
            startGame();
        }
    }

    public static void startGame() {
        UHCAPI.getInstance().getGameHandler().getGameConfig().setStarting(true);
        new LaunchGameTask();
    }
}
