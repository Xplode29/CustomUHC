package me.butter.impl.commands.host;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.item.list.MenuItem;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoHostCommand extends AbstractCommand {
    public CoHostCommand() {
        super("cohost");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(!sender.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Seul le host peut utiliser cette commande !"));
            return;
        }

        if(args.length < 3) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h cohost <add|remove> <joueur>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[2]);

        if(target == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[2] + " n'existe pas !"));
            return;
        }

        if(args[1].equals("add")) {
            if(UHCAPI.getInstance().getGameHandler().getGameConfig().getCoHosts().contains(target)) {
                sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[2] + " est déja un co-host !"));
                return;
            }
            UHCAPI.getInstance().getItemHandler().giveItemToPlayer(MenuItem.class, target);
            UHCAPI.getInstance().getGameHandler().getGameConfig().addCoHost(target);

            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(target.getName() + " est maintenant un co-host."));
        }
        else if (args[1].equals("remove")) {
            if(!UHCAPI.getInstance().getGameHandler().getGameConfig().getCoHosts().contains(target)) {
                sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[2] + " n'est pas un co-host !"));
                return;
            }
            UHCAPI.getInstance().getItemHandler().removeItemFromPlayer(MenuItem.class, target);
            UHCAPI.getInstance().getMenuHandler().closeMenu(target);

            UHCAPI.getInstance().getGameHandler().getGameConfig().removeCoHost(target);

            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(target.getName() + " n'est maintenant plus un co-host."));
        }
        else {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h cohost <add|remove> <joueur>"));
        }
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length == 2) {
            return Lists.newArrayList("add", "remove");
        }
        if(args.length == 3) {
            if(args[1].equals("add")) {
                return UHCAPI.getInstance().getPlayerHandler().getAllPlayers().stream()
                        .filter(uhcPlayer -> !UHCAPI.getInstance().getGameHandler().getGameConfig().getCoHosts().contains(uhcPlayer))
                        .map(UHCPlayer::getName)
                        .collect(Collectors.toList());
            }
            if(args[1].equals("remove")) {
                return UHCAPI.getInstance().getPlayerHandler().getAllPlayers().stream()
                        .filter(uhcPlayer -> UHCAPI.getInstance().getGameHandler().getGameConfig().getCoHosts().contains(uhcPlayer))
                        .map(UHCPlayer::getName)
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}
