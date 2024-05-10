package me.butter.impl.game.configs;

import me.butter.api.game.configs.ItemConfig;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemConfigImpl implements ItemConfig {

    boolean rod, enderPearl, bow, projectile, lavaBucket, flintAndSteel;

    List<ItemStack> startingInventory, startingArmor, deathInventory;

    public ItemConfigImpl() {
        this.rod = false;
        this.enderPearl = false;
        this.bow = false;
        this.projectile = false;
        this.lavaBucket = false;
        this.flintAndSteel = false;
        this.startingInventory = new ArrayList<>();
        this.startingArmor = new ArrayList<>();
        this.deathInventory = new ArrayList<>();
    }

    @Override
    public boolean isRod() {
        return rod;
    }

    @Override
    public void setRod(boolean rod) {
        this.rod = rod;
    }

    @Override
    public boolean isEnderPearl() {
        return enderPearl;
    }

    @Override
    public void setEnderPearl(boolean enderPearl) {
        this.enderPearl = enderPearl;
    }

    @Override
    public boolean isBow() {
        return bow;
    }

    @Override
    public void setBow(boolean bow) {
        this.bow = bow;
    }

    @Override
    public boolean isProjectile() {
        return projectile;
    }

    @Override
    public void setProjectile(boolean projectile) {
        this.projectile = projectile;
    }

    @Override
    public boolean isLavaBucket() {
        return lavaBucket;
    }

    @Override
    public void setLavaBucket(boolean lavaBucket) {
        this.lavaBucket = lavaBucket;
    }

    @Override
    public boolean isFlintAndSteel() {
        return flintAndSteel;
    }

    @Override
    public void setFlintAndSteel(boolean flintAndSteel) {
        this.flintAndSteel = flintAndSteel;
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
