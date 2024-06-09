package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.item.list.MenuItem;
import me.butter.impl.menu.list.host.MainMenu;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.stream.Collectors;

public class ConfigCommand extends AbstractCommand {
    public ConfigCommand() {
        super("config");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        UHCAPI.getInstance().getMenuHandler().openMenu(sender, new MainMenu(), false);
    }
}
