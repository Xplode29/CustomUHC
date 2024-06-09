package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.items.SpinjitzuPower;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Kai extends NinjagoRole {

    boolean nextToNya;

    public Kai() {
        super("Kai", "/roles/ninjas/kai", Arrays.asList(
                new FlameBow(),
                new FireAspectBook(),
                new SpinjitzuPower(ChatColor.RED)
        ));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Fire Resistance permanent ainsi que 10% de force a 10 blocs de nya",
                "A l'annonce des roles, vous obtenez un Arc power 3 flame 1"
        };
    }

    @Override
    public boolean isNinja() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addPotionEffect(PotionEffectType.FIRE_RESISTANCE, -1, 1);
    }

    @Override
    public void onDistributionFinished() {
        UHCPlayer nya = null;
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Nya && role.getUHCPlayer() != null) {
                nya = role.getUHCPlayer();
            }
        }

        if (nya != null) {
            UHCPlayer finalNya = nya;
            Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
                if(getUHCPlayer() == null) {
                    return;
                }

                if(getUHCPlayer().isNextTo(finalNya, 10) && !nextToNya) {
                    getUHCPlayer().addStrength(10);
                    nextToNya = true;
                }
                else if(!getUHCPlayer().isNextTo(finalNya, 10) && nextToNya) {
                    getUHCPlayer().removeStrength(10);
                    nextToNya = false;
                }
            }, 20, 20);
        }
    }

    private static class FlameBow extends RightClickItemPower {

        public FlameBow() {
            super("Arc de flamme", Material.BOW, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Un arc enchanté Power 3 Flame 1."};
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.BOW)
                    .setName(getName())
                    .addEnchant(Enchantment.ARROW_DAMAGE, 3)
                    .addEnchant(Enchantment.ARROW_FIRE, 1)
                    .setUnbreakable()
                    .toItemStack();
        }

        @Override
        public boolean doesCancelEvent() {
            return false;
        }
    }

    private static class FireAspectBook extends EnchantBookPower {
        public FireAspectBook() {
            super("Livre Fire Aspect", Enchantment.FIRE_ASPECT, 1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Un livre enchanté fire aspect 1. Il est possible de le fusionner avec son épée en diamant."};
        }
    }
}
