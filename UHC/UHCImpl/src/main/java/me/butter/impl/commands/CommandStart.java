package me.butter.impl.commands;

import me.butter.api.UHCAPI;
import me.butter.impl.task.LaunchGameTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandStart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (UHCAPI.get().getGameHandler().getGameConfig().isStarting()) {
            UHCAPI.get().getGameHandler().getGameConfig().setStarting(false);
        } else {
            UHCAPI.get().getGameHandler().getGameConfig().setStarting(true);
            new LaunchGameTask();
        }

        return true;
    }
}
