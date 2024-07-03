package me.butter.ninjago.roles.list.serpents;

import me.butter.api.UHCAPI;
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

    private UHCPlayer pythor;

    public Arcturus() {
        super("Arcturus", "/roles/serpent-10/arcturus");
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez §cForce 1§r la nuit",
                "A l'annonce des roles, vous obtenez le pseudo de §1Pythor§r.",
                "Lorsque vous retirez votre armure, vous obtenez §bInvisibilité§r et §2No fall§r."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de §1Pythor§r" : "§1Pythor§r:" + pythor.getName()));
    }

    @Override
    public void onGiveRole() {
        if(!UHCAPI.getInstance().getGameHandler().getGameConfig().isDay()) {
            getUHCPlayer().addStrength(15);
        }

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
                getUHCPlayer().setNoFall(false);
                getUHCPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            }
            if(invisible && !getUHCPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                getUHCPlayer().setNoFall(true);
                getUHCPlayer().addPotionEffect(PotionEffectType.INVISIBILITY, -1, 1);
            }
        }, 0, 10);
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
    public void onDay() {
        getUHCPlayer().removeStrength(15);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addStrength(15);
    }
}
