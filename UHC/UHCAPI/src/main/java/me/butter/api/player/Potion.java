package me.butter.api.player;

import org.bukkit.potion.PotionEffectType;

public interface Potion {

    PotionEffectType getEffect(); void setEffect(PotionEffectType effect);

    int getDuration(); void setDuration(int duration);

    int getLevel(); void setLevel(int level);

    boolean isActive(); void setActive(boolean active);

    boolean isValid();
}
