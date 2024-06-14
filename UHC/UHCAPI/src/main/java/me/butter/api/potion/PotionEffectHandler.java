package me.butter.api.potion;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public interface PotionEffectHandler {
    List<CustomPotionEffect> getPotionEffects();

    void setPotionEffects(List<CustomPotionEffect> potionEffects);

    void addPotionEffect(CustomPotionEffect potionEffect);

    void removePotionEffect(CustomPotionEffect potionEffect);

    CustomPotionEffect getEffect(PotionEffectType effect);
}
