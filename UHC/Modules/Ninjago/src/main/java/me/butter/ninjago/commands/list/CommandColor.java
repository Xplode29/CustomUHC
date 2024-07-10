package me.butter.ninjago.commands.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.menu.list.player.color.PlayerNametagMenu;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandColor extends AbstractCommand {
    public CommandColor() {
        super("color");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length == 1) {
            sender.openMenu(new PlayerNametagMenu(), false);
            return;
        }

        if(args.length < 3) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Usage: /color <player> <color>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);
        if(target == null) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Ce joueur n'existe pas !"));
            return;
        }

        if(UHCAPI.getInstance().getNametagColorHandler().getAvaliableColors().keySet().stream().noneMatch(color -> color.equalsIgnoreCase(args[2]))) {
            sender.sendMessage(ChatUtils.ERROR.getMessage("Cette couleur n'existe pas !"));
            return;
        }

        ChatColor color = UHCAPI.getInstance().getNametagColorHandler().getAvaliableColors().get(args[2]);

        UHCAPI.getInstance().getNametagColorHandler().setPlayerToTeam(
                sender,
                color,
                target
        );
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length == 2) {
            return UHCAPI.getInstance().getPlayerHandler().getPlayersConnected().stream().map(UHCPlayer::getName).collect(Collectors.toList());
        }
        if(args.length == 3) {
            return new ArrayList<>(UHCAPI.getInstance().getNametagColorHandler().getAvaliableColors().keySet());
        }
        return new ArrayList<>();
    }
}
