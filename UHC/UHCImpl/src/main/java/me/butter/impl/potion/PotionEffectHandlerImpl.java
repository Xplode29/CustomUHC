package me.butter.impl.potion;

import me.butter.api.potion.CustomPotionEffect;
import me.butter.api.potion.PotionEffectHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionEffectHandlerImpl implements PotionEffectHandler {

    List<CustomPotionEffect> potionEffects;

    public PotionEffectHandlerImpl() {
        potionEffects = new ArrayList<>();

        potionEffects.add(new PotionEffectImpl("Regeneration", PotionEffectType.REGENERATION, 2, 2, true, true));
        potionEffects.add(new PotionEffectImpl("Vitesse", PotionEffectType.SPEED, 2, 2, true, true));
        potionEffects.add(new PotionEffectImpl("Resistance au feu", PotionEffectType.FIRE_RESISTANCE, 1, 1, true, true));
        potionEffects.add(new PotionEffectImpl("Soin instantane", PotionEffectType.HEAL, 1, 1, true, false));
        potionEffects.add(new PotionEffectImpl("Force", PotionEffectType.INCREASE_DAMAGE, 2, 2, true, true));
        potionEffects.add(new PotionEffectImpl("Saut", PotionEffectType.JUMP, 2, 2, true, true));
        potionEffects.add(new PotionEffectImpl("Respiration", PotionEffectType.WATER_BREATHING, 1, 1, true, true));
        potionEffects.add(new PotionEffectImpl("Invisibilite", PotionEffectType.INVISIBILITY, 1, 1, true, true));

        potionEffects.add(new PotionEffectImpl("Poison", PotionEffectType.POISON, 2, 2, true, true));
        potionEffects.add(new PotionEffectImpl("Faiblesse", PotionEffectType.WEAKNESS, 1, 1, true, true));
        potionEffects.add(new PotionEffectImpl("Lenteur", PotionEffectType.SLOW, 1, 1, true, true));
        potionEffects.add(new PotionEffectImpl("Degats instantanes", PotionEffectType.HARM, 2, 2, true, false));
    }

    @Override
    public List<CustomPotionEffect> getPotionEffects() {
        return potionEffects;
    }

    @Override
    public void setPotionEffects(List<CustomPotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
    }

    @Override
    public void addPotionEffect(CustomPotionEffect potionEffect) {
        if(!potionEffects.contains(potionEffect))
            potionEffects.add(potionEffect);
    }

    @Override
    public void removePotionEffect(CustomPotionEffect potionEffect) {
        potionEffects.remove(potionEffect);
    }

    @Override
    public CustomPotionEffect getEffect(PotionEffectType effect) {
        return potionEffects.stream().filter(potionEffect -> potionEffect.getEffect() == effect).findFirst().orElse(null);
    }
}
