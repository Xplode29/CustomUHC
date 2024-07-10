package me.butter.impl.commands;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class CommandsRemover {
    public static void clearBukkitCommands() {
        Map<String, Command> knownCommands;
        CommandMap commandMap = getCommandMaps();
        try {
            Field knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
            knownCommandsField.setAccessible(true);
            knownCommands = (Map<String, Command>) knownCommandsField.get(commandMap);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        List<String> list = Lists.newArrayList();
        for (Map.Entry<String, Command> entry : knownCommands.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("me")) list.add(entry.getKey());
            if (entry.getKey().equalsIgnoreCase("say")) list.add(entry.getKey());
            if (entry.getKey().startsWith("minecraft:")) list.add(entry.getKey());
            if (entry.getKey().startsWith("worldedit:")) list.add(entry.getKey());
        }

        for (String s : list) {
            knownCommands.remove(s);
        }
    }

    public static CommandMap getCommandMaps() {
        Field bukkitCommandMapField;
        CommandMap commandMap;
        try {
            bukkitCommandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        bukkitCommandMapField.setAccessible(true);
        try {
            commandMap = (CommandMap) bukkitCommandMapField.get(Bukkit.getServer());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return commandMap;
    }

}
