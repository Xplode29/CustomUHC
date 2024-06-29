package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.menu.list.player.InventoryViewMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewInvCommand extends AbstractCommand {
    public ViewInvCommand() {
        super("viewinv");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 2) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h viewinv <joueur>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        sender.openMenu(new InventoryViewMenu(target.getInventory(), target.getArmor()), false);
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length == 2) {
            return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
