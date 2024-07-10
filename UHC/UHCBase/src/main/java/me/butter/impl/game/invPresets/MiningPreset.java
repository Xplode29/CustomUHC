package me.butter.impl.game.invPresets;

import me.butter.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class MiningPreset extends AbstractPreset {

    public MiningPreset() {
        super("Minage", Material.IRON_PICKAXE, new String[] {});

        addToStartingInventory(0, new ItemBuilder(Material.IRON_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 3).setUnbreakable().build());
        addToStartingInventory(1, new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.DIG_SPEED, 3).setUnbreakable().build());
        addToStartingInventory(2, new ItemBuilder(Material.IRON_SPADE).addEnchant(Enchantment.DIG_SPEED, 3).setUnbreakable().build());

        addToStartingInventory(4, new ItemStack(Material.WATER_BUCKET));
        addToStartingInventory(5, new ItemStack(Material.COOKED_BEEF, 64));
        addToStartingInventory(6, new ItemStack(Material.ARROW, 64));
        addToStartingInventory(7, new ItemStack(Material.BOOK, 12));

        addToStartingInventory(15, new ItemStack(Material.LOG, 64));
        addToStartingInventory(16, new ItemStack(Material.LOG, 64));
        addToStartingInventory(17, new ItemStack(Material.LOG, 64));
    }
}
