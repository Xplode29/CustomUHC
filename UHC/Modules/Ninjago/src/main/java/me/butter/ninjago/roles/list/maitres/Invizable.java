package me.butter.ninjago.roles.list.maitres;

import me.butter.api.module.power.TargetPlayerItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Invizable extends NinjagoRole {

    List<String> maitres;

    public Invizable() {
        super("Invizable", "/roles/alliance-des-elements-4/invizable", new BlindPowerPlayer());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez §9Speed 1§r permanent.",
                "Lorsque vous enlevez votre armure, vous obtenez §bInvisibilité§r, §2No Fall§r, §cForce 1§r et §7Resistance 2§r. ",
                "Vous connaissez la liste des membres de l'alliance"
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Membres de l'alliance: " + String.join(", ", maitres));
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(20);

        Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
            if(getUHCPlayer().getPlayer() == null) return;

            boolean invisible = true;
            for(ItemStack item : getUHCPlayer().getPlayer().getInventory().getArmorContents()) {
                if(item.getType() != Material.AIR) {
                    invisible = false;
                    break;
                }
            }

            if(!invisible && getUHCPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                getUHCPlayer().removeStrength(15);
                getUHCPlayer().removeResi(40);
                getUHCPlayer().setNoFall(false);
                getUHCPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                getUHCPlayer().setNameTagVisible(true);
            }
            if(invisible && !getUHCPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                getUHCPlayer().addStrength(15);
                getUHCPlayer().addResi(40);
                getUHCPlayer().setNoFall(true);
                getUHCPlayer().addPotionEffect(PotionEffectType.INVISIBILITY, -1, 1);
                getUHCPlayer().setNameTagVisible(false);
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

    private static class BlindPowerPlayer extends TargetPlayerItemPower {
        public BlindPowerPlayer() {
            super("Rayonnement", Material.NETHER_STAR, 20, 5 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Aveugle le joueur vise pendant 10 secondes (Distance maximum: 20 blocks)"
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, Action clickAction) {
            target.addPotionEffect(PotionEffectType.BLINDNESS, 10, 1);

            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez aveuglé " + target.getName()));
            return true;
        }
    }

}
