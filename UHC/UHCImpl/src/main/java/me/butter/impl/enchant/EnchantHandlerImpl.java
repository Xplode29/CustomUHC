package me.butter.impl.enchant;

import me.butter.api.enchant.Enchant;
import me.butter.api.enchant.EnchantHandler;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;

public class EnchantHandlerImpl implements EnchantHandler {

    List<Enchant> enchants;

    public EnchantHandlerImpl() {
        this.enchants = new ArrayList<>();

        this.enchants.add(new EnchantImpl(Enchantment.PROTECTION_ENVIRONMENTAL, "Protection", 4));
        this.enchants.add(new EnchantImpl(Enchantment.PROTECTION_EXPLOSIONS, "Projectile Protection", 4));
        this.enchants.add(new EnchantImpl(Enchantment.PROTECTION_FIRE, "Fire Protection", 4));
        this.enchants.add(new EnchantImpl(Enchantment.PROTECTION_PROJECTILE, "Projectile Protection", 4));

        this.enchants.add(new EnchantImpl(Enchantment.OXYGEN, "Respiration", 3));
        this.enchants.add(new EnchantImpl(Enchantment.WATER_WORKER, "Aqua Affinity", 1));
        this.enchants.add(new EnchantImpl(Enchantment.THORNS, "Thorns", 3));
        this.enchants.add(new EnchantImpl(Enchantment.DEPTH_STRIDER, "Depth Strider", 3));
        this.enchants.add(new EnchantImpl(Enchantment.PROTECTION_FALL, "Feather Falling", 4));

        this.enchants.add(new EnchantImpl(Enchantment.DAMAGE_ALL, "Sharpness", 5));
        this.enchants.add(new EnchantImpl(Enchantment.DAMAGE_UNDEAD, "Smite", 5));
        this.enchants.add(new EnchantImpl(Enchantment.DAMAGE_ARTHROPODS, "Bane Of Arthropods", 5));

        this.enchants.add(new EnchantImpl(Enchantment.KNOCKBACK, "Knockback", 2));
        this.enchants.add(new EnchantImpl(Enchantment.FIRE_ASPECT, "Fire Aspect", 2));
        this.enchants.add(new EnchantImpl(Enchantment.LOOT_BONUS_MOBS, "Looting", 3));

        this.enchants.add(new EnchantImpl(Enchantment.DIG_SPEED, "Efficiency", 5));
        this.enchants.add(new EnchantImpl(Enchantment.SILK_TOUCH, "Silk Touch", 1));
        this.enchants.add(new EnchantImpl(Enchantment.DURABILITY, "Unbreaking", 3));
        this.enchants.add(new EnchantImpl(Enchantment.LOOT_BONUS_BLOCKS, "Fortune", 3));

        this.enchants.add(new EnchantImpl(Enchantment.ARROW_DAMAGE, "Power", 5));
        this.enchants.add(new EnchantImpl(Enchantment.ARROW_FIRE, "Flame", 1));
        this.enchants.add(new EnchantImpl(Enchantment.ARROW_INFINITE, "Infinity", 1));
        this.enchants.add(new EnchantImpl(Enchantment.ARROW_KNOCKBACK, "Punch", 2));

        this.enchants.add(new EnchantImpl(Enchantment.LUCK, "Luck of the Sea", 3));
        this.enchants.add(new EnchantImpl(Enchantment.LURE, "Lure", 3));
    }

    @Override
    public List<Enchant> getEnchants() {
        return enchants;
    }

    @Override
    public void setEnchants(List<Enchant> enchants) {
        this.enchants = enchants;
    }

    @Override
    public void addEnchant(Enchant enchant) {
        if(!this.enchants.contains(enchant))
            this.enchants.add(enchant);
    }

    @Override
    public void removeEnchant(Enchant enchant) {
        this.enchants.remove(enchant);
    }

    @Override
    public Enchant getEnchant(Enchantment enchantment) {
        return enchants.stream().filter(enchant -> enchant.getEnchantment() == enchantment).findFirst().orElse(null);
    }
}
