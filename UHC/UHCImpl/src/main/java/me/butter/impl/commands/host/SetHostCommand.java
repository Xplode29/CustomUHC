package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.item.list.MenuItem;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.stream.Collectors;

public class SetHostCommand extends AbstractCommand {
    public SetHostCommand() {
        super("sethost");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 2) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h sethost <joueur>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        if(target.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " est déja le host de la partie !"));
            return;
        }

        if(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() != null) {
            UHCAPI.getInstance().getItemHandler().removeItemFromPlayer(MenuItem.class, UHCAPI.getInstance().getGameHandler().getGameConfig().getHost());
        }
        UHCAPI.getInstance().getItemHandler().giveItemToPlayer(MenuItem.class, target);

        UHCAPI.getInstance().getGameHandler().getGameConfig().setHost(target);

        Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(target.getName() + " est maintenant le host de la partie."));
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
    }
}
