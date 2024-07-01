package me.butter.impl.commands;

import me.butter.api.player.UHCPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand {

    private String argument;
    private String[] aliases;

    public AbstractCommand(String argument, String... aliases) {
        this.argument = argument;
        this.aliases = aliases;
    }

    public void onCommand(UHCPlayer sender, String command, String[] args) {

    }

    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        return new ArrayList<>();
    }

    public String getArgument() {
        return argument;
    }

    public String[] getAliases() {
        return aliases;
    }

    public boolean isHidden() {
        return false;
    }
}
