package me.butter.api.enchant;

import org.bukkit.enchantments.Enchantment;

import java.util.List;

public interface EnchantHandler {

    List<Enchant> getEnchants(); void setEnchants(List<Enchant> enchants);

    void addEnchant(Enchant enchant);

    void removeEnchant(Enchant enchant);

    Enchant getEnchant(Enchantment enchantment);
}
