package me.butter.ninjago.roles.list.solos;

import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.items.SpinjitzuPower;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import me.butter.ninjago.roles.list.ninjas.Wu;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;

import java.util.Collections;

public class Garmadon extends NinjagoRole {
    public Garmadon() {
        super("Garmadon", "/roles/solitaires/garmadon", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez Speed 1, Résistance 1 et 12 coeurs permanents. ",
                "Lorsque vous tuez Wu, vous obtenez Force 1 permanent. ",
                "Lorsque vous tuez Lloyd, vous obtenez l'accès au Spinjitzu ainsi qu'un livre Protection 3 (applicable sur le diamant)."
        };
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(20);
        getUHCPlayer().addResi(20);
        getUHCPlayer().addMaxHealth(4);
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller() != getUHCPlayer() || event.getVictim().getRole() == null) return;
        if(event.getVictim().getRole() instanceof Wu) {
            getUHCPlayer().addStrength(20);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu Force 1 permanent."));
        }
        if(event.getVictim().getRole() instanceof Lloyd) {
            ProtectionBook book = new ProtectionBook();
            addPower(book);
            getUHCPlayer().giveItem(book.getItem(), true);

            SpinjitzuPower spinjitzuPower = new SpinjitzuPower(ChatColor.DARK_PURPLE);
            addPower(spinjitzuPower);
            getUHCPlayer().giveItem(spinjitzuPower.getItem(), true);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu l'accès au Spinjitzu ainsi qu'un livre Protection 3."));
        }
    }

    private static class ProtectionBook extends EnchantBookPower {
        public ProtectionBook() {
            super("§rLivre Protection 3", Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Un livre enchanté protection 3. Il est possible de le fusionner avec une piece en diamant."};
        }
    }
}
