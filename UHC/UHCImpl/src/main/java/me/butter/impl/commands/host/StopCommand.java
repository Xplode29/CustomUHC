package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.commands.AbstractCommand;

public class StopCommand extends AbstractCommand {
    public StopCommand() {
        super("stop");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if (UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
            UHCAPI.getInstance().getGameHandler().getGameConfig().setStarting(false);
        }
        if(UHCAPI.getInstance().getGameHandler().getGameState() != GameState.LOBBY) {
            UHCAPI.getInstance().getServer().reload();
        }
    }
}
