package me.butter.ninjago.structures;

import me.butter.impl.structures.AbstractStructure;
import me.butter.ninjago.items.goldenWeapons.AbstractGoldenWeapon;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

public class StructChestHolder extends AbstractStructure {

    private Chest chest;
    private AbstractGoldenWeapon weapon;

    public StructChestHolder(int x, int y, int z, World world) {
        super("Gold Weapons Stand.schematic", x, y, z, world);
    }

    @Override
    public void onSpawn() {
        if(getWorld().getBlockAt(getX() + 3, getY() + 1, getZ() + 3).getState() instanceof Chest) {
            chest = (Chest) getWorld().getBlockAt(getX() + 3, getY() + 1, getZ() + 3).getState();
        }
    }

    public AbstractGoldenWeapon getWeapon() {
        return weapon;
    }

    public void setWeapon(AbstractGoldenWeapon weapon) {
        this.weapon = weapon;
        setItemInChest(13, weapon.getItemStack());
    }

    public void clearChest() {
        this.weapon = null;
        chest.getBlockInventory().clear();
    }

    public Chest getChest() {
        return chest;
    }

    public void setItemInChest(int slot, ItemStack item) {
        if(chest == null || chest.getBlockInventory() == null) return;
        chest.getBlockInventory().setItem(slot, item);
    }
}
