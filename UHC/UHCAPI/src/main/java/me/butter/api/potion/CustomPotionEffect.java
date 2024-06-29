package me.butter.api.potion;

import org.bukkit.potion.PotionEffectType;

public interface CustomPotionEffect {

    String getName();

    PotionEffectType getEffect();

    int getMaxLevel();

    int getLevel(); void setLevel(int level);

    boolean canBeSplash(); void setSplash(boolean splash);

    boolean canBeAmplified(); void setAmplified(boolean amplified);
}
