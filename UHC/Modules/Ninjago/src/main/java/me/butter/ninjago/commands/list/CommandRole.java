package me.butter.ninjago.commands.list;

import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatSnippets;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

public class CommandRole extends AbstractCommand {
    public CommandRole() {
        super("role", "me");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(sender.getRole() == null) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez pas de role !"));
            return;
        }
        ChatSnippets.rolePresentation(sender);
    }
}
