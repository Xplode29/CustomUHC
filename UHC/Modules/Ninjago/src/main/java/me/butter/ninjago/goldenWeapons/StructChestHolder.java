package me.butter.ninjago.goldenWeapons;

import me.butter.impl.structures.AbstractStructure;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

public class StructChestHolder extends AbstractStructure {

    private Block chestBlock;
    private AbstractGoldenWeapon weapon;

    public StructChestHolder(int x, int y, int z, World world) {
        super("Gold Weapons Stand.schematic", x, y, z, world);
    }

    @Override
    public void onSpawn() {
        if(getWorld().getBlockAt(getX() + 3, getY() + 1, getZ() + 3).getState() instanceof Chest) {
            chestBlock = getWorld().getBlockAt(getX() + 3, getY() + 1, getZ() + 3);
        }
    }

    public AbstractGoldenWeapon getWeapon() {
        return weapon;
    }

    public void setWeapon(AbstractGoldenWeapon weapon) {
        this.weapon = weapon;
    }

    public void clearChest() {
        chestBlock.setType(Material.AIR);
        this.weapon = null;
    }

    public Block getBlockChest() {
        return chestBlock;
    }
}
