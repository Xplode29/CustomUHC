package me.butter.api.clickablechat;

import me.butter.api.player.UHCPlayer;

import java.util.List;

public interface ClickableChatCommand {

    String getArgument();
    int getParametersSize();
    UHCPlayer getUHCPlayer();

    void send();

    void onCommand(List<String> args);
}
