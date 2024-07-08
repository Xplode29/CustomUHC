package me.butter.impl.enchant;

import me.butter.api.enchant.Enchant;
import org.bukkit.enchantments.Enchantment;

public class EnchantImpl implements Enchant {

    private Enchantment enchantment;
    private String name;
    private boolean enabled;
    private int allLimit, diamondLimit, maxLevel;

    public EnchantImpl(Enchantment enchantment, String name, int maxLevel) {
        this.enchantment = enchantment;
        this.name = name;
        this.maxLevel = maxLevel;

        this.allLimit = maxLevel;
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
    public int getAllLevel() {
        return allLimit;
    }

    @Override
    public void setAllLevel(int allLimit) {
        this.allLimit = allLimit;
    }

    @Override
    public int getDiamondLevel() {
        return diamondLimit;
    }

    @Override
    public void setDiamondLevel(int diamondLimit) {
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
