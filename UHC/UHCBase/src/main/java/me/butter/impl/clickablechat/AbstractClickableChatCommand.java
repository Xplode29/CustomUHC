package me.butter.impl.clickablechat;

import me.butter.api.clickablechat.ClickableChatCommand;
import me.butter.api.player.UHCPlayer;

public abstract class AbstractClickableChatCommand implements ClickableChatCommand {

    UHCPlayer player;
    String argument;
    int parametersSize;

    public AbstractClickableChatCommand(UHCPlayer player, String argument, String... parameters) {
        this.player = player;
        this.argument = argument;
        this.parametersSize = parameters.length;
    }

    @Override
    public String getArgument() {
        return argument;
    }

    @Override
    public int getParametersSize() {
        return parametersSize;
    }

    @Override
    public UHCPlayer getUHCPlayer() {
        return player;
    }
}
