package me.butter.ninjago.commands.list;

import me.butter.api.player.UHCPlayer;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.menu.list.player.FullMenu;

public class CommandFull extends AbstractCommand {

    public CommandFull() {
        super("full");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        sender.openMenu(new FullMenu(), false);
    }
}
