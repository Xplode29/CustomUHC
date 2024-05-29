package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeleportCommand extends AbstractCommand {
    public TeleportCommand() {
        super("teleport", "tp");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 2) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h teleport <joueur> [joueur|monde]"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        if(args.length == 2) {
            sender.getPlayer().teleport(target.getPlayer());
            sender.getPlayer().sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Vous avez ete teleporte vers " + target.getPlayer().getName()));
            return;
        }
        else {
            for (World world : Bukkit.getWorlds()) {
                if(world.getName().equalsIgnoreCase(args[2])) {
                    target.getPlayer().teleport(new Location(world, 0, 100, 0));
                    target.sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Vous avez ete teleporte vers le monde " + world.getName()));
                    break;
                }
            }
            for(UHCPlayer uhcPlayer1 : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
                if(uhcPlayer1.getPlayer().getName().equalsIgnoreCase(args[2])) {
                    target.getPlayer().teleport(uhcPlayer1.getPlayer());
                    target.sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Vous avez ete teleporte vers " + uhcPlayer1.getPlayer().getName()));
                    break;
                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length == 2) {
            return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
        }
        if(args.length == 3) {
            return Stream.concat(Bukkit.getWorlds().stream().map(World::getName),
                    UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName)
            ).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
