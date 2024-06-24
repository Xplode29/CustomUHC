package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.menu.list.player.InventoryViewMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChatCommand extends AbstractCommand {
    public ChatCommand() {
        super("chat");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        UHCAPI.getInstance().getGameHandler().getGameConfig().setChatEnabled(!UHCAPI.getInstance().getGameHandler().getGameConfig().isChatEnabled());

        sender.getPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Le chat est maintenant " + (UHCAPI.getInstance().getGameHandler().getGameConfig().isChatEnabled() ? "active" : "desactive")));
    }
}
