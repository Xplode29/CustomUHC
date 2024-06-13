package me.butter.ninjago.commands.list;

import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatSnippets;
import me.butter.impl.commands.AbstractCommand;

public class ListEffectsCommand extends AbstractCommand {
    public ListEffectsCommand() {
        super("effects");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        ChatSnippets.listEffects(sender);
    }
}
