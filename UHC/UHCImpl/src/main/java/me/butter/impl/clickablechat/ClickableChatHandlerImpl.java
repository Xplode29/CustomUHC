package me.butter.impl.clickablechat;

import me.butter.api.UHCAPI;
import me.butter.api.clickablechat.ClickableChatCommand;
import me.butter.api.clickablechat.ClickableChatHandler;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClickableChatHandlerImpl implements ClickableChatHandler, Listener {

    List<ClickableChatCommand> remainingClickableMessages;

    public static String commandPrefix = "gegregdgfhjgazxdgfrhfghfdhgfhgf";

    public ClickableChatHandlerImpl() {
        remainingClickableMessages = new ArrayList<>();

        UHCAPI.getInstance().getServer().getPluginManager().registerEvents(this, UHCAPI.getInstance());
    }

    @Override
    public void sendToPlayer(ClickableChatCommand command) {
        command.send();
        remainingClickableMessages.add(command);
    }

    @Override
    public void onClickChatMessage(UHCPlayer player, String prefix, List<String> args) {
        for(ClickableChatCommand chatCommand : remainingClickableMessages) {
            if(chatCommand.getUHCPlayer() != player) continue;

            if(chatCommand.getArgument().equals(prefix)) {
                if(chatCommand.getParametersSize() > args.size()) return;
                chatCommand.onCommand(args);

                remainingClickableMessages.remove(chatCommand);
                return;
            }
        }
    }

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
