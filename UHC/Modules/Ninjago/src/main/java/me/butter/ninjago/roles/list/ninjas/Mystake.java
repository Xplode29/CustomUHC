package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.TargetPlayerItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;

import java.util.Random;

public class Mystake extends NinjagoRole {

    private boolean isOni = false;

    public Mystake() {
        super("Mystake", "/roles/ninjas/mystake");
        addPower(new TravellerTea());
        addPower(new OniCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous n'avez pas d'effet particuliers."
        };
    }

    @Override
    public void onDay() {
        if(isOni) {
            getUHCPlayer().removeStrength(15);
        }
    }

    @Override
    public void onNight() {
        if(isOni) {
            getUHCPlayer().addStrength(15);
        }
    }

    private class TravellerTea extends TargetPlayerItemPower {

        public TravellerTea() {
            super("The de Voyageur", Material.GLASS_BOTTLE, 20, 10 * 60, 3);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Teleporte le joueur vise aleatoirement sur la map."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, Action clickAction) {
            if(isOni) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous ne pouvez plus utiliser ce pouvoir !"));
                return false;
            }
            int randomX = (int) (new Random().nextInt((int) target.getPlayer().getWorld().getWorldBorder().getSize() - 20) - target.getPlayer().getWorld().getWorldBorder().getSize() / 2 + 10);
            int randomZ = (int) (new Random().nextInt((int) target.getPlayer().getWorld().getWorldBorder().getSize() - 20) - target.getPlayer().getWorld().getWorldBorder().getSize() / 2 + 10);

            target.getPlayer().teleport(new Location(target.getPlayer().getWorld(), randomX, target.getPlayer().getWorld().getHighestBlockYAt(randomX, randomZ), randomZ));

            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez teleporte " + target.getName() + "!"));
            return true;
        }
    }

    private class OniCommand extends CommandPower {

        public OniCommand() {
            super("Forme Oni", "oni", 0, 1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Active votre forme oni. Vous gagnez alors Resistance permanent.",
                    "Cependant, vous obtenez Faiblesse 1 le jour et vous ne pouvez plus utiliser le the du voyageur.",
                    "Attention, cette forme ne peut pas être annulée."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            isOni = true;
            player.addResi(20);

            if(UHCAPI.getInstance().getGameHandler().getGameConfig().isDay()) {
                player.removeStrength(15);
            }

            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez active la forme oni !"));
            return true;
        }
    }
}
