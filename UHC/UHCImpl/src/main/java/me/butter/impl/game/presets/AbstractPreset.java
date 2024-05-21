package me.butter.impl.game.presets;

import me.butter.api.game.Preset;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public abstract class AbstractPreset implements Preset {

    List<ItemStack> startingInventory;
    List<ItemStack> startingArmor;

    String name;
    String[] description;
    Material icon;

    public AbstractPreset(String name, Material icon, String[] description) {
        this.name = name;
        this.description = description;
        this.icon = icon;

        this.startingInventory = new ArrayList<>();
        this.startingArmor = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getDescription() {
        return description;
    }

    @Override
    public Material getIcon() {
        return icon;
    }

    @Override
    public List<ItemStack> getStartingInventory() {
        return startingInventory;
    }

    @Override
    public List<ItemStack> getStartingArmor() {
        return startingArmor;
    }

    @Override
    public void addToStartingInventory(int slot, ItemStack item) {
        if(slot >= startingInventory.size()) {
            for(int i = startingInventory.size(); i <= slot; i++) {
                startingInventory.add(null);
            }
        }
        startingInventory.set(slot, item);
    }

    @Override
    public void addToStartingArmor(int slot, ItemStack item) {
        if(slot >= startingArmor.size()) {
            for(int i = startingArmor.size(); i <= slot; i++) {
                startingArmor.add(null);
            }
        }

        startingArmor.set(slot, item);
    }
}
