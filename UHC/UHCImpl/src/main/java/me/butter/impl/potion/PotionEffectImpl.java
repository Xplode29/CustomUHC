package me.butter.impl.potion;

import me.butter.api.potion.CustomPotionEffect;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionEffectImpl implements CustomPotionEffect {

    String name;
    PotionEffectType effect;
    int level, maxLevel;
    boolean splash, amplified;

    public PotionEffectImpl(String name, PotionEffectType effect, int level, int maxLevel, boolean splash, boolean amplified) {
        this.name = name;
        this.effect = effect;
        this.level = level;
        this.maxLevel = maxLevel;
        this.splash = splash;
        this.amplified = amplified;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PotionEffectType getEffect() {
        return effect;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean canBeSplash() {
        return splash;
    }

    @Override
    public void setSplash(boolean splash) {
        this.splash = splash;
    }

    @Override
    public boolean canBeAmplified() {
        return amplified;
    }

    @Override
    public void setAmplified(boolean amplified) {
        this.amplified = amplified;
    }
}
