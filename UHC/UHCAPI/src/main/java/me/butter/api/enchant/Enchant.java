package me.butter.api.enchant;

import org.bukkit.enchantments.Enchantment;

public interface Enchant {

    Enchantment getEnchantment();

    String getName();

    int getIronLimit(); void setIronLimit(int ironLimit);

    int getDiamondLimit(); void setDiamondLimit(int diamondLimit);

    int getMaxLevel();

    boolean isEnabled(); void setEnabled(boolean enabled);
}
