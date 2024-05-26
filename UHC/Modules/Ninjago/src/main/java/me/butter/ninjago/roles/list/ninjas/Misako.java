package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.menu.MisakoMenu;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Misako extends NinjagoRole {
    public Misako() {
        super("Misako", "doc", Arrays.asList(
            new InventoryCommand()
        ));
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez 10% de speed permanent. "
        };
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(10);
    }

    private static class InventoryCommand extends TargetCommandPower {

        public InventoryCommand() {
            super("Inventaire", "inventory", 20 * 60, 3);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"A l'execution, vous pouvez voir l'inventaire du joueur ciblé"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            player.openMenu(new MisakoMenu(target), false);

            return true;
        }
    }
}
