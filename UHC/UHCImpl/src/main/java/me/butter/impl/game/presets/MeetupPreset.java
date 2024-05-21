package me.butter.impl.game.presets;

import me.butter.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class MeetupPreset extends AbstractPreset {

    public MeetupPreset() {
        super("Meetup", Material.DIAMOND_CHESTPLATE, new String[] {});

        addToStartingInventory(0, new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.DAMAGE_ALL, 3).toItemStack());
        addToStartingInventory(1, new ItemStack(Material.GOLDEN_APPLE, 24));
        addToStartingInventory(2, new ItemBuilder(Material.BOW).addEnchant(Enchantment.ARROW_DAMAGE, 3).toItemStack());
        addToStartingInventory(3, new ItemStack(Material.LAVA_BUCKET));
        addToStartingInventory(4, new ItemStack(Material.WATER_BUCKET));
        addToStartingInventory(5, new ItemStack(Material.COOKED_BEEF, 64));
        addToStartingInventory(6, new ItemStack(Material.WATER_LILY, 64));
        addToStartingInventory(7, new ItemStack(Material.WOOD, 64));
        addToStartingInventory(8, new ItemStack(Material.WOOD, 64));

        addToStartingInventory(27, new ItemBuilder(Material.DIAMOND_PICKAXE).addEnchant(Enchantment.DIG_SPEED, 3).setInfinityDurability().toItemStack());
        addToStartingInventory(28, new ItemBuilder(Material.DIAMOND_AXE).addEnchant(Enchantment.DIG_SPEED, 3).setInfinityDurability().toItemStack());
        addToStartingInventory(29, new ItemStack(Material.ARROW, 64));
        addToStartingInventory(30, new ItemStack(Material.LAVA_BUCKET));
        addToStartingInventory(31, new ItemStack(Material.WATER_BUCKET));
        addToStartingInventory(33, new ItemStack(Material.WOOD, 64));
        addToStartingInventory(34, new ItemStack(Material.WOOD, 64));
        addToStartingInventory(35, new ItemStack(Material.WOOD, 64));

        addToStartingInventory(21, new ItemStack(Material.LAVA_BUCKET));
        addToStartingInventory(22, new ItemStack(Material.WATER_BUCKET));
        addToStartingInventory(24, new ItemStack(Material.WOOD, 64));
        addToStartingInventory(25, new ItemStack(Material.WOOD, 64));
        addToStartingInventory(26, new ItemStack(Material.WOOD, 64));

        addToStartingInventory(15, new ItemStack(Material.LOG, 64));
        addToStartingInventory(16, new ItemStack(Material.LOG, 64));
        addToStartingInventory(17, new ItemStack(Material.LOG, 64));

        addToStartingArmor(0, new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
        addToStartingArmor(1, new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
        addToStartingArmor(2, new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).addEnchant(Enchantment.DURABILITY, 3).toItemStack());
        addToStartingArmor(3, new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 2).toItemStack());
    }
}
