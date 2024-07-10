package me.butter.impl.commands.host;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecCommand extends AbstractCommand {
    public SpecCommand() {
        super("spec");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 3) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h spec <add|remove> <joueur>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[2]);

        if(target == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[2] + " n'existe pas !"));
            return;
        }

        if(args[1].equals("add")) {
            if(target.getPlayerState() == PlayerState.IN_SPEC) {
                sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[2] + " est déja un spectateur !"));
                return;
            }
            target.setPlayerState(PlayerState.IN_SPEC);
            target.getPlayer().setGameMode(GameMode.SPECTATOR);

            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(target.getName() + " est maintenant un spectateur."));
        }
        else if (args[1].equals("remove")) {
            if(UHCAPI.getInstance().getGameHandler().getGameState() != GameState.LOBBY) {
                sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous ne pouvez pas retirer un spectateur pendant la partie !"));
                return;
            }

            if(target.getPlayerState() != PlayerState.IN_SPEC) {
                sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[2] + " n'est pas un spectateur !"));
                return;
            }

            target.setPlayerState(PlayerState.IN_LOBBY);
            target.getPlayer().setGameMode(GameMode.SURVIVAL);

            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(target.getName() + " n'est maintenant plus un spectateur."));
        }
        else {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h spec <add|remove> <joueur>"));
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
                        .filter(uhcPlayer -> uhcPlayer.getPlayerState() != PlayerState.IN_SPEC)
                        .map(UHCPlayer::getName).collect(Collectors.toList());
            }
            if(args[1].equals("remove")) {
                return UHCAPI.getInstance().getPlayerHandler().getAllPlayers().stream()
                        .filter(uhcPlayer -> uhcPlayer.getPlayerState() == PlayerState.IN_SPEC)
                        .map(UHCPlayer::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}
