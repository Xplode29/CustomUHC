package me.butter.ninjago.roles.list.solos;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import me.butter.ninjago.roles.list.ninjas.Wu;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import java.util.Collections;

public class Garmadon extends NinjagoRole {
    public Garmadon() {
        super("Garmadon", "/roles/solitaires/garmadon");
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
    public boolean isElementalMaster() {
        return true;
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

            SpinjitzuPower spinjitzuPower = new SpinjitzuPower();
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

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.DARK_PURPLE + "Spinjitzu", Material.NETHER_STAR, 10 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {"À l'activation, repousse tous les joueurs dans un rayon de 10 blocks."};
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            Location center = player.getPlayer().getLocation();

            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                if(uhcPlayer == null || uhcPlayer == player || uhcPlayer.getPlayer() == null) continue;

                if(uhcPlayer.isNextTo(player, 10)) {
                    double angle = Math.atan2(uhcPlayer.getLocation().getZ() - center.getZ(), uhcPlayer.getLocation().getX() - center.getX());
                    Vector newVelocity = new Vector(
                            1.5 * Math.cos(angle),
                            0.5,
                            1.5 * Math.sin(angle)
                    );
                    uhcPlayer.getPlayer().setVelocity(newVelocity);
                }
            }

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.PURPLE);
            return true;
        }
    }
}
