package me.butter.api.utils.chat;

import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatSnippets {

    public static void joinMessage(org.bukkit.entity.Player player) {
        player.sendMessage(ChatUtils.SEPARATOR + "");
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("Bienvenue sur l'UHC !"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.SEPARATOR + "");
    }

    public static void rolePresentation(UHCPlayer uhcPlayer) {
        org.bukkit.entity.Player player = uhcPlayer.getPlayer();

        player.sendMessage(ChatUtils.SEPARATOR + "");
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("Vous êtes §r" + uhcPlayer.getRole().getCamp().getPrefix() + uhcPlayer.getRole().getName()));

        player.sendMessage(ChatUtils.JOINED.getMessage("Vous devez gagner aves les §r" + uhcPlayer.getRole().getCamp().getPrefix() + uhcPlayer.getRole().getCamp().getName()));
        player.sendMessage("");

        player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Description:"));
        //Before the role presentation
        for(String line : uhcPlayer.getRole().getDescription()) {
            player.sendMessage(ChatUtils.NORMAL.getMessage(line));
        }
        player.sendMessage("");

        //Items
        if(uhcPlayer.getRole().hasItems()) {
            player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Items"));
            for(Power power : uhcPlayer.getRole().getPowers()) {
                if(power instanceof ItemPower && power.showPower()) {
                    player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage(
                            power.getName() + "§r: " + String.join(" ", power.getDescription()) +
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
        if(uhcPlayer.getRole().hasCommands()) {
            player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Commandes"));
            for(Power power : uhcPlayer.getRole().getPowers()) {
                if(power instanceof CommandPower && power.showPower()) {
                    CommandPower commandPower = (CommandPower) power;
                    player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage(
                            power.getName() + "§r (/ni " + commandPower.getArgument() + "): " + String.join(" ", power.getDescription()) +
                                    (power.hideCooldowns() ? "" : (
                                        " (" + ((power.getMaxUses() > 0) ? (power.getMaxUses() + " Utilisation(s)") : "Utilisation infinie") +
                                        " / " + ((power.getCooldown() > 0) ? (GraphicUtils.convertToAccurateTime(power.getCooldown())) : "Pas de cooldown") + ")"
                                    ))
                    ));
                }
            }
            player.sendMessage("");
        }

        player.sendMessage(ChatUtils.SEPARATOR + "");

        //After the role presentation
        for(String line : uhcPlayer.getRole().additionalDescription()) {
            player.sendMessage(ChatUtils.NORMAL.getMessage(line));
        }
    }

    public static void listEffects(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();
        player.sendMessage(ChatUtils.SEPARATOR + "");
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("Strength: " + uhcPlayer.getStrength() + "%"));
        player.sendMessage(ChatUtils.JOINED.getMessage("Resistance: " + uhcPlayer.getResi() + "%"));
        player.sendMessage(ChatUtils.JOINED.getMessage("Speed: " + uhcPlayer.getSpeed() + "%"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.SEPARATOR + "");
    }

    public static void playerDeath(UHCPlayer uhcPlayer) {
        Bukkit.broadcastMessage(ChatUtils.SEPARATOR + "");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatUtils.JOINED.getMessage("§rLe joueur " + uhcPlayer.getName() + "§r est mort."));
        if(uhcPlayer.getRole() != null) Bukkit.broadcastMessage(ChatUtils.JOINED.getMessage("§rIl était " + uhcPlayer.getRole().getName()));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatUtils.SEPARATOR + "");
    }

    public static void listGameCommands(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();
        player.sendMessage(ChatUtils.SEPARATOR + "");
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("/ni me: Description de votre role"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("/ni roles: Liste des roles dans la partie"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("/ni effects: Vos pourcentages d'effets"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.SEPARATOR + "");
    } // TO DO

    public static void listHostCommands(UHCPlayer uhcPlayer) {
        Player player = uhcPlayer.getPlayer();
        player.sendMessage(ChatUtils.SEPARATOR + "");
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("/host start: Lancer la partie"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("/host stop: Arréter la partie"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.JOINED.getMessage("/host save: Sauvegarder l'inventaire de départ"));
        player.sendMessage("");
        player.sendMessage(ChatUtils.SEPARATOR + "");
    } // TO DO
}
