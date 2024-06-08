package me.butter.impl.clickablechat;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.List;

public class ClickChatCommand implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        UHCPlayer sender = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(sender == null) return;

        String[] message = event.getMessage().split(" ");

        if(message.length > 1 && message[0].equals(ClickableChatHandlerImpl.commandPrefix)) {
            List<String> args = Arrays.asList(message).subList(2, message.length);
            Bukkit.getScheduler().runTaskLater(UHCImpl.getInstance(), () -> UHCAPI.getInstance().getClickableChatHandler().onClickChatMessage(sender, message[1], args), 5);
            event.setCancelled(true);
        }
    }
}
