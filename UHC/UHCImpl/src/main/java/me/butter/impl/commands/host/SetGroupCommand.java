package me.butter.impl.commands.host;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.item.list.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SetGroupCommand extends AbstractCommand {
    public SetGroupCommand() {
        super("setgroupe");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 2) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h setgroupe <taille>"));
            return;
        }

        int amount = Integer.parseInt(args[1]);

        if(amount <= 0) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le taille des groupe n'est pas valide"));
            return;
        }

        UHCAPI.getInstance().getGameHandler().getGameConfig().setGroupSize(amount);

        for(UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
            player.sendTitle("Groupe de " + amount, ChatColor.AQUA);
        }
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length == 2) {
            return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
        }
        if(args.length == 3) {
            return Lists.newArrayList("add", "remove");
        }
        return new ArrayList<>();
    }
}
