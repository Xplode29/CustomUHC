package me.butter.impl.player;

import me.butter.api.player.Potion;
import org.bukkit.potion.PotionEffectType;

public class PotionImpl implements Potion {

    private PotionEffectType effect;
    private int duration;
    private int level;
    private boolean active;

    public PotionImpl(PotionEffectType effect, int duration, int level) {
        this.effect = effect;
        this.duration = duration;
        this.level = level;
        this.active = true;
    }

    @Override
    public PotionEffectType getEffect() {
        return effect;
    }

    @Override
    public void setEffect(PotionEffectType effect) {
        this.effect = effect;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
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
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isValid() {
        return (effect != null) && (level > 0);
    }
}
