package me.butter.api.module.power;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.Enchant;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class EnchantBookPower extends ItemPower {

    Enchantment enchant;
    int level;

    public EnchantBookPower(String name, Enchantment enchant, int level) {
        super(name, Material.ENCHANTED_BOOK, 0, -1);

        this.level = level;
        this.enchant = enchant;
    }

    @Override
    public ItemStack getItem() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = book.getItemMeta();
        itemMeta.setDisplayName(getName());
        ((EnchantmentStorageMeta) itemMeta).addStoredEnchant(enchant, level, true);
        book.setItemMeta(itemMeta);
        return book;
    }

    @Override
    public boolean doesCancelEvent() {
        return false;
    }

    @Override
    public void onUsePower(UHCPlayer player, Action clickAction) {

    }

    @Override
    public boolean hideCooldowns() {
        return true;
    }

    @Override
    public boolean canMoveItem() {
        return true;
    }
}
