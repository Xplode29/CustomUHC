package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.menu.list.host.MainMenu;

public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        UHCAPI.getInstance().getMenuHandler().openMenu(sender, new MainMenu(), false);
    }
}
