package me.butter.api.clickablechat;

import me.butter.api.player.UHCPlayer;

import java.util.List;

public interface ClickableChatHandler {

    void sendToPlayer(ClickableChatCommand command);

    void onClickChatMessage(UHCPlayer player, String prefix, List<String> args);
}
