package me.butter.api.game;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Preset {

    String getName();

    String[] getDescription();

    Material getIcon();

    List<ItemStack> getStartingInventory(); void addToStartingInventory(int slot, ItemStack item);
    List<ItemStack> getStartingArmor(); void addToStartingArmor(int slot, ItemStack item);
}
