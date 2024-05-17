package me.butter.api.enchant;

import org.bukkit.enchantments.Enchantment;

public interface Enchant {

    Enchantment getEnchantment();

    String getName();

    int getIronLevel(); void setIronLevel(int ironLimit);

    int getDiamondLevel(); void setDiamondLevel(int diamondLimit);

    int getMaxLevel();

    boolean isEnabled(); void setEnabled(boolean enabled);
}
