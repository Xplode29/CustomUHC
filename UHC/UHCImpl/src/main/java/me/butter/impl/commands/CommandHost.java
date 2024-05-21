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
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class CommandHost implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return true;

        if(strings.length == 0) {
                if(player.isOp()) {
                    if(uhcPlayer.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + uhcPlayer.getName() + " est déja le host de la partie !"));
                        return true;
                    }

                    if(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() != null) {
                        UHCAPI.getInstance().getItemHandler().removeItemFromPlayer(MenuItem.class, UHCAPI.getInstance().getGameHandler().getGameConfig().getHost());
                    }
                    UHCAPI.getInstance().getItemHandler().giveItemToPlayer(MenuItem.class, uhcPlayer);

                    UHCAPI.getInstance().getGameHandler().getGameConfig().setHost(uhcPlayer);

                    Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(uhcPlayer.getName() + " est maintenant le host de la partie."));
                    return true;
                }
        }

        else {
            if(
                    UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ||
                    !uhcPlayer.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())
            ) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Tu n'es pas le host de la partie !"));
                return true;
            }

            UHCPlayer target = null;
            if(strings.length > 1) {
                target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(strings[1]);
            }

            switch (strings[0]) {
                case "stop":
                    if (UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
                        UHCAPI.getInstance().getGameHandler().getGameConfig().setStarting(false);
                    }
                    if(UHCAPI.getInstance().getGameHandler().getGameState() != GameState.LOBBY) {
                        UHCAPI.getInstance().getServer().reload();
                    }
                    return true;
                case "start":
                    if (!UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
                        UHCAPI.getInstance().getGameHandler().getGameConfig().setStarting(true);
                        new LaunchGameTask();
                    }
                    return true;
                case "save":
                    List<ItemStack> armor = Arrays.asList(uhcPlayer.getPlayer().getInventory().getArmorContents());
                    Collections.reverse(armor);
                    UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingArmor(armor);

                    UHCAPI.getInstance().getGameHandler().getInventoriesConfig().setStartingInventory(
                            Arrays.asList(uhcPlayer.getPlayer().getInventory().getContents())
                    );

                    uhcPlayer.clearInventory();

                    uhcPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);

                    UHCAPI.getInstance().getItemHandler().giveLobbyItems(uhcPlayer);
                    return true;
                case "sethost":
                    if(strings.length < 2) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Usage : /h sethost <joueur>"));
                        return true;
                    }

                    if(target == null) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + strings[1] + " n'existe pas !"));
                        return true;
                    }

                    if(target.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + strings[1] + " est déja le host de la partie !"));
                        return true;
                    }

                    if(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() != null) {
                        UHCAPI.getInstance().getItemHandler().removeItemFromPlayer(MenuItem.class, UHCAPI.getInstance().getGameHandler().getGameConfig().getHost());
                    }
                    UHCAPI.getInstance().getItemHandler().giveItemToPlayer(MenuItem.class, target);

                    UHCAPI.getInstance().getGameHandler().getGameConfig().setHost(target);

                    Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(target.getName() + " est maintenant le host de la partie."));

                    return true;
                case "effect":
                    if(strings.length < 5) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Usage : /h effect <joueur> <add|remove> <speed|force|resistance> <amount>"));
                        return true;
                    }

                    if(target == null) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + strings[1] + " n'existe pas !"));
                        return true;
                    }

                    int amount = Integer.parseInt(strings[4]);

                    if(strings[2].equalsIgnoreCase("add")) {
                        if(strings[3].equalsIgnoreCase("speed")) {
                            target.addSpeed(amount);
                            return true;
                        }
                        else if(strings[3].equalsIgnoreCase("force")) {
                            target.addStrength(amount);
                            return true;
                        }
                        else if(strings[3].equalsIgnoreCase("resistance")) {
                            target.addResi(amount);
                            return true;
                        }
                    }
                    else if(strings[2].equalsIgnoreCase("remove")) {
                        if(strings[3].equalsIgnoreCase("speed")) {
                            target.removeSpeed(amount);
                            return true;
                        }
                        else if(strings[3].equalsIgnoreCase("force")) {
                            target.removeStrength(amount);
                            return true;
                        }
                        else if(strings[3].equalsIgnoreCase("resistance")) {
                            target.removeResi(amount);
                            return true;
                        }
                    }

                    player.sendMessage(ChatUtils.ERROR.getMessage("Usage : /h effect <joueur> <add|remove> <speed|force|resistance>"));
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
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);

        if(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null || !uhcPlayer.equals(UHCAPI.getInstance().getGameHandler().getGameConfig().getHost())) {
            return new ArrayList<>();
        }

        if(strings.length == 1) {
            return Lists.newArrayList("effect", "start", "save", "sethost", "stop");
        }

        if(strings.length > 1) {
            switch (strings[0]) {
                case "start":
                    return new ArrayList<>();
                case "stop":
                    return new ArrayList<>();
                case "sethost":
                    return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
                case "effect":
                    if(strings.length == 2) {
                        return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
                    }
                    if(strings.length == 3) {
                        return Lists.newArrayList("add", "remove");
                    }
                    if(strings.length == 4) {
                        return Lists.newArrayList("speed", "force", "resistance");
                    }
                    return new ArrayList<>();
                default:
                    return new ArrayList<>();
            }
        }
        return Collections.emptyList();
    }
}
