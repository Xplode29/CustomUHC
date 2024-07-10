package me.butter.ninjago.commands.list;

import me.butter.api.player.UHCPlayer;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.menu.list.player.RulesViewMenu;

public class CommandRules extends AbstractCommand {
    public CommandRules() {
        super("rules");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        sender.openMenu(new RulesViewMenu(), false);
    }
}
