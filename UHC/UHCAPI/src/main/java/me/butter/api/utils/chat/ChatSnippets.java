package me.butter.api.utils.chat;

import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatSnippets {
    public static void rolePresentation(UHCPlayer player) {
        Role role = player.getRole();
        if(role == null) return;

        player.sendMessage(ChatUtils.LINE + "");
        player.sendMessage("");
        player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage("Vous êtes §r" + role.getCamp().getPrefix() + role.getName()));

        player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage("Vous devez gagner aves les §r" + role.getCamp().getPrefix() + role.getCamp().getName()));
        player.sendMessage("");

        player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Description:"));
        //Before the role presentation
        for(String line : role.getDescription()) {
            player.sendMessage(ChatUtils.INDENTED.getMessage(line));
        }
        player.sendMessage("");

        //Items
        if(role.hasItems()) {
            player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Items"));
            for(Power power : role.getPowers()) {
                if(power instanceof ItemPower && power.showPower()) {
                    player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage(
                            power.getName() + "§r: " + String.join("\n ", power.getDescription()) +
                                    (power.hideCooldowns() ? "" : (
                                        " (" + ((power.getMaxUses() > 0) ? (power.getMaxUses() + " Utilisation(s)") : "Utilisation infinie") +
                                        " / " + ((power.getCooldown() > 0) ? (GraphicUtils.convertToAccurateTime(power.getCooldown())) : "Pas de cooldown") + ")"
                                    ))
                    ));
                }
            }
            player.sendMessage("");
        }

        //Commands
        if(role.hasCommands()) {
            player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Commandes"));
            for(Power power : role.getPowers()) {
                if(power instanceof CommandPower && power.showPower()) {
                    CommandPower commandPower = (CommandPower) power;
                    player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage(
                            power.getName() + "§r (/n " + commandPower.getArgument() + "): " + String.join("\n ", power.getDescription()) +
                                    (power.hideCooldowns() ? "" : (
                                        " (" + ((power.getMaxUses() > 0) ? (power.getMaxUses() + " Utilisation(s)") : "Utilisation infinie") +
                                        " / " + ((power.getCooldown() > 0) ? (GraphicUtils.convertToAccurateTime(power.getCooldown())) : "Pas de cooldown") + ")"
                                    ))
                    ));
                }
            }
            player.sendMessage("");
        }

        player.sendMessage(ChatUtils.LINE + "");

        //After the role presentation
        for(String line : role.additionalDescription()) {
            player.sendMessage(ChatUtils.INDENTED.getMessage(line));
        }
    }

    public static void listEffects(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();
        player.sendMessage(ChatUtils.LINE + "");
        player.sendMessage("");
        player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage("Strength: " + uhcPlayer.getStrength() + "%"));
        player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage("Resistance: " + uhcPlayer.getResi() + "%"));
        player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage("Speed: " + uhcPlayer.getSpeed() + "%"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.LINE + "");
    }

    public static void playerDeath(UHCPlayer uhcPlayer) {
        Bukkit.broadcastMessage(ChatUtils.LINE + "");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatUtils.LIST_ELEMENT.getMessage("§rLe joueur " + uhcPlayer.getName() + "§r est mort."));
        if(uhcPlayer.getRole() != null) Bukkit.broadcastMessage(ChatUtils.LIST_ELEMENT.getMessage("§rIl était " + uhcPlayer.getRole().getCamp().getPrefix() + uhcPlayer.getRole().getName()));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatUtils.LINE + "");
    }
}
