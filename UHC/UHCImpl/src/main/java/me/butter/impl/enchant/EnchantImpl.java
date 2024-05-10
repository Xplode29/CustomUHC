package me.butter.impl.enchant;

import me.butter.api.enchant.Enchant;
import org.bukkit.enchantments.Enchantment;

public class EnchantImpl implements Enchant {

    Enchantment enchantment;

    String name;

    boolean enabled;

    int ironLimit, diamondLimit, maxLevel;

    public EnchantImpl(Enchantment enchantment, String name, int maxLevel) {
        this.enchantment = enchantment;
        this.name = name;
        this.maxLevel = maxLevel;

        this.ironLimit = maxLevel;
        this.diamondLimit = maxLevel;
        this.enabled = true;
    }

    @Override
    public Enchantment getEnchantment() {
        return enchantment;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getIronLimit() {
        return ironLimit;
    }

    @Override
    public void setIronLimit(int ironLimit) {
        this.ironLimit = ironLimit;
    }

    @Override
    public int getDiamondLimit() {
        return diamondLimit;
    }

    @Override
    public void setDiamondLimit(int diamondLimit) {
        this.diamondLimit = diamondLimit;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
