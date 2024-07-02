package me.butter.api.module.power;

import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class EnchantedItemPower extends ItemPower {

    Map<Enchantment, Integer> enchants;

    public EnchantedItemPower(String name, Material mat, Map<Enchantment, Integer> enchants) {
        super(name, mat, 0, -1);
        this.enchants = enchants;
    }

    @Override
    public ItemStack getItem() {
        ItemBuilder builder = new ItemBuilder(getMaterial())
                .setName("§r" + getName())
                .setUnbreakable();
        for(Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
            builder.addEnchant(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

    @Override
    public boolean doesCancelEvent() {
        return false;
    }

    @Override
    public void onUsePower(UHCPlayer player, Action clickAction) {

    }

    @Override
    public boolean hideCooldowns() {
        return true;
    }
}
