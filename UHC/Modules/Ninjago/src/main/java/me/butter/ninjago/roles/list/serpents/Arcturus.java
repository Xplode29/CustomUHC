package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Arcturus extends NinjagoRole {

    UHCPlayer pythor;

    boolean invisible = false;

    public Arcturus() {
        super("Arcturus", "/roles/serpent/bytar", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 la nuit",
                "A l'annonce des roles, vous obtenez le pseudo de Pythor.",
                "Lorsque vous retirez votre armure, vous obtenez Invisibilité et No fall"
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de Pythor" : "Pythor:" + pythor.getName()));
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Pythor && role.getUHCPlayer() != null) {
                pythor = role.getUHCPlayer();
                break;
            }
        }
    }

    @Override
    public void onGiveRole() {
        Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
            for(ItemStack item : getUHCPlayer().getPlayer().getInventory().getArmorContents()) {
                if(item.getType() != Material.AIR) {
                    if(invisible) {
                        getUHCPlayer().setNoFall(false);
                        getUHCPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                        invisible = false;
                    }
                    return;
                }
            }
            if(!invisible) {
                getUHCPlayer().setNoFall(true);
                getUHCPlayer().addPotionEffect(PotionEffectType.INVISIBILITY, -1, 1);
                invisible = true;
            }
        }, 0, 10);
    }

    @Override
    public void onDay() {
        getUHCPlayer().removeStrength(20);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addStrength(20);
    }
}
