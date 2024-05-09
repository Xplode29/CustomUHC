package me.butter.api.player;

import org.bukkit.potion.PotionEffectType;

public interface Potion {

    PotionEffectType getEffect(); void setEffect(PotionEffectType potionEffectType);

    int getDuration(); void setDuration();

    int getLevel(); void setLevel();

    boolean isActive(); void setActive(boolean active);
}
