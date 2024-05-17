package me.butter.impl.commands;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ChatUtils;
import me.butter.impl.item.list.MenuItem;
import me.butter.impl.task.LaunchGameTask;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandHost implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return true;

        if(strings.length == 0) {
                if(player.isOp()) {
                    if(uhcPlayer.equals(UHCAPI.get().getGameHandler().getGameConfig().getHost())) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + uhcPlayer.getName() + " est déja le host de la partie !"));
                        return true;
                    }

                    if(UHCAPI.get().getGameHandler().getGameConfig().getHost() != null) {
                        UHCAPI.get().getItemHandler().removeItemFromPlayer(MenuItem.class, UHCAPI.get().getGameHandler().getGameConfig().getHost());
                    }
                    UHCAPI.get().getItemHandler().giveItemToPlayer(MenuItem.class, uhcPlayer);

                    UHCAPI.get().getGameHandler().getGameConfig().setHost(uhcPlayer);

                    Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(uhcPlayer.getName() + " est maintenant le host de la partie."));
                    return true;
                }
        }

        else {
            if(
                    UHCAPI.get().getGameHandler().getGameConfig().getHost() == null ||
                    !uhcPlayer.equals(UHCAPI.get().getGameHandler().getGameConfig().getHost())
            ) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Tu n'es pas le host de la partie !"));
                return true;
            }

            switch (strings[0]) {
                case "stop":
                    if (UHCAPI.get().getGameHandler().getGameConfig().isStarting()) {
                        UHCAPI.get().getGameHandler().getGameConfig().setStarting(false);
                    }
                    if(UHCAPI.get().getGameHandler().getGameState() != GameState.LOBBY) {
                        UHCAPI.get().getServer().reload();
                    }
                    return true;
                case "start":
                    if (!UHCAPI.get().getGameHandler().getGameConfig().isStarting()) {
                        UHCAPI.get().getGameHandler().getGameConfig().setStarting(true);
                        new LaunchGameTask();
                    }
                    return true;
                case "save":
                    UHCAPI.get().getGameHandler().getItemConfig().setStartingArmor(
                            Arrays.asList(uhcPlayer.getPlayer().getInventory().getArmorContents())
                    );
                    UHCAPI.get().getGameHandler().getItemConfig().setStartingInventory(
                            Arrays.asList(uhcPlayer.getPlayer().getInventory().getContents())
                    );

                    uhcPlayer.clearInventory();

                    uhcPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);

                    UHCAPI.get().getItemHandler().giveLobbyItems(uhcPlayer);
                    return true;
                case "sethost":
                    if(strings.length < 2) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Usage : /h sethost <joueur>"));
                        return true;
                    }

                    UHCPlayer target = UHCAPI.get().getPlayerHandler().getUHCPlayer(strings[1]);

                    if(target == null) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + strings[1] + " n'existe pas !"));
                        return true;
                    }

                    if(target.equals(UHCAPI.get().getGameHandler().getGameConfig().getHost())) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + strings[1] + " est déja le host de la partie !"));
                        return true;
                    }

                    if(UHCAPI.get().getGameHandler().getGameConfig().getHost() != null) {
                        UHCAPI.get().getItemHandler().removeItemFromPlayer(MenuItem.class, UHCAPI.get().getGameHandler().getGameConfig().getHost());
                    }
                    UHCAPI.get().getItemHandler().giveItemToPlayer(MenuItem.class, target);

                    UHCAPI.get().getGameHandler().getGameConfig().setHost(target);

                    Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(target.getName() + " est maintenant le host de la partie."));

                    return true;
                default:
                    player.sendMessage(ChatUtils.ERROR.getMessage("Usage : /h <start|stop|sethost|save>"));
                    return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return new ArrayList<>();

        Player player = (Player) commandSender;
        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);

        if(UHCAPI.get().getGameHandler().getGameConfig().getHost() == null || !uhcPlayer.equals(UHCAPI.get().getGameHandler().getGameConfig().getHost())) {
            return new ArrayList<>();
        }

        if(strings.length == 1) {
            return Lists.newArrayList("start", "stop", "sethost", "save");
        }

        if(strings.length == 2) {
            switch (strings[0]) {
                case "start":
                    return new ArrayList<>();
                case "stop":
                    return new ArrayList<>();
                case "sethost":
                    return UHCAPI.get().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
                default:
                    return new ArrayList<>();
            }
        }
        return Collections.emptyList();
    }
}
