package me.butter.impl.clickablechat;

import me.butter.api.clickablechat.ClickableChatCommand;
import me.butter.api.clickablechat.ClickableChatHandler;
import me.butter.api.player.UHCPlayer;

import java.util.ArrayList;
import java.util.List;

public class ClickableChatHandlerImpl implements ClickableChatHandler {

    List<ClickableChatCommand> remainingClickableMessages;

    public static String commandPrefix = "gegregdgfhjgazxdgfrhfghfdhgfhgf";

    public ClickableChatHandlerImpl() {
        remainingClickableMessages = new ArrayList<>();
    }

    @Override
    public void sendToPlayer(ClickableChatCommand command) {
        command.send();
        remainingClickableMessages.add(command);
    }

    @Override
    public void onClickChatMessage(UHCPlayer player, String prefix, List<String> args) {
        for(ClickableChatCommand chatCommand : remainingClickableMessages) {
            if(chatCommand.getUHCPlayer() != player) continue;

            if(chatCommand.getArgument().equals(prefix)) {
                if(chatCommand.getParametersSize() > args.size()) return;
                chatCommand.onCommand(args);

                remainingClickableMessages.remove(chatCommand);
                return;
            }
        }
    }
}
