package me.butter.impl.game.configs;

import me.butter.api.game.Preset;
import me.butter.api.game.configs.InventoryConfig;
import me.butter.impl.game.invPresets.MeetupPreset;
import me.butter.impl.game.invPresets.MiningPreset;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryConfigImpl implements InventoryConfig {

    private List<Preset> presets = new ArrayList<>();

    private List<ItemStack> startingInventory, startingArmor, deathInventory;

    public InventoryConfigImpl() {
        this.startingInventory = new ArrayList<>();
        this.startingArmor = new ArrayList<>();
        this.deathInventory = new ArrayList<>();

        presets.add(new MeetupPreset());
        presets.add(new MiningPreset());
    }

    @Override
    public List<Preset> getPresetList() {
        return presets;
    }

    @Override
    public Preset getPreset(Class<? extends Preset> presetClass) {
        return presets.stream().filter(p -> p.getClass() == presetClass).findFirst().orElse(null);
    }

    @Override
    public List<ItemStack> getStartingInventory() {
        return startingInventory;
    }

    @Override
    public void setStartingInventory(List<ItemStack> startingInventory) {
        this.startingInventory = startingInventory;
    }

    @Override
    public List<ItemStack> getStartingArmor() {
        return startingArmor;
    }

    @Override
    public void setStartingArmor(List<ItemStack> startingArmor) {
        this.startingArmor = startingArmor;
    }

    @Override
    public List<ItemStack> getDeathInventory() {
        return deathInventory;
    }

    @Override
    public void setDeathInventory(List<ItemStack> deathInventory) {
        this.deathInventory = deathInventory;
    }
}
