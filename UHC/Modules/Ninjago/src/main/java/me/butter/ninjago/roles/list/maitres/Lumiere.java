package me.butter.ninjago.roles.list.maitres;

import me.butter.api.module.power.TargetItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Lumiere extends NinjagoRole {

    List<String> maitres;

    boolean invisible = false;

    public Lumiere() {
        super("Invizable (Maitre de la lumiere)", "doc", Collections.singletonList(new BlindPower()));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous avez 12 coeurs permanents.",
                "Lorsque vous enlevez votre armure, vous obtenez invisibilité, speed 1 et no fall. ",
                "Vous conaissez la liste des maitres"
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Maitres: " + String.join(", ", maitres));
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addMaxHealth(4);

        Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
            if(getUHCPlayer().getPlayer() == null) return;

            for(ItemStack item : getUHCPlayer().getPlayer().getInventory().getArmorContents()) {
                if(item.getType() != Material.AIR) {
                    if(invisible) {
                        getUHCPlayer().setNoFall(false);
                        getUHCPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                        getUHCPlayer().removeSpeed(20);
                        invisible = false;
                    }
                    return;
                }
            }
            if(!invisible) {
                getUHCPlayer().setNoFall(true);
                getUHCPlayer().addPotionEffect(PotionEffectType.INVISIBILITY, -1, 1);
                getUHCPlayer().addSpeed(20);
                invisible = true;
            }
        }, 0, 10);
    }

    @Override
    public void onDistributionFinished() {
        maitres = new ArrayList<>();

        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role.getUHCPlayer() != null && role.getCamp().equals(CampEnum.MASTER.getCamp())) {
                maitres.add(role.getUHCPlayer().getName());
            }
        }
    }

    private static class BlindPower extends TargetItemPower {

        public BlindPower() {
            super(ChatColor.WHITE + "Rayonnement§r", Material.NETHER_STAR, 20, 3, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Faire un clique droit en visant un joueur avec l'item lui donnera blindness pendant 10 secondes (Distance maximum: 20 blocks)"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, Action clickAction) {
            target.addPotionEffect(PotionEffectType.BLINDNESS, 10, 1);
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ébloui " + target.getName()));
            return true;
        }
    }

}
