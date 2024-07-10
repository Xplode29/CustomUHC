package me.butter.ninjago.commands.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

public class CommandDoc extends AbstractCommand {

    public CommandDoc() {
        super("doc");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        String url = UHCAPI.getInstance().getModuleHandler().getModule().getDoc();
        if(sender.getRole() != null) url += sender.getRole().getDoc();
        sender.sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Documentation: " + url));
    }
}
