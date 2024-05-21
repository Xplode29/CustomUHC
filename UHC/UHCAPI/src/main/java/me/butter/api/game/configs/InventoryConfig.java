package me.butter.api.game.configs;

import me.butter.api.game.Preset;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface InventoryConfig {

    List<Preset> getPresetList();

    Preset getPreset(Class<? extends Preset> presetClass);

    List<ItemStack> getStartingInventory(); void setStartingInventory(List<ItemStack> startingInventory);

    List<ItemStack> getStartingArmor(); void setStartingArmor(List<ItemStack> startingArmor);

    List<ItemStack> getDeathInventory(); void setDeathInventory(List<ItemStack> deathInventory);
}
