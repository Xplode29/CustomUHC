package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Dareth extends NinjagoRole {
    public Dareth() {
        super("Dareth", "soon", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez faiblesse le jour.",
                "A l'annonce des roles, vous obtenez un arc enchanté power 3 et punch 1. "
        };
    }

    @Override
    public void onGiveRole() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta itemMeta = bow.getItemMeta();
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 3, true);
        itemMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, true);
        bow.setItemMeta(itemMeta);
        getUHCPlayer().giveItem(bow, true);
    }

    @Override
    public void onDay() {
        getUHCPlayer().addPotionEffect(PotionEffectType.WEAKNESS, -1, 1);
    }

    @Override
    public void onNight() {
        getUHCPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
    }
}
