package me.butter.impl.commands.host;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import org.bukkit.Bukkit;

import java.util.List;

public class SayCommand extends AbstractCommand {
    public SayCommand() {
        super("say");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        List<String> message = Lists.newArrayList(args);
        message.remove("say");

        if (message.isEmpty()) {
            sender.sendMessage("§cUsage: /" + command + " <message>");
            return;
        }

        Bukkit.broadcastMessage(ChatUtils.LINE.prefix);
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("§lHOST " + sender.getName() + " §r» " + Joiner.on(" ").join(message));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatUtils.LINE.prefix);
    }
}
