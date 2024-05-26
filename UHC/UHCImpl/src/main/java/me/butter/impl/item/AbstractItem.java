package me.butter.impl.item;

import me.butter.api.item.CustomItem;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractItem implements CustomItem {

    Material material;
    String name;

    public AbstractItem(Material material, String name) {
        this.material = material;
        this.name = "§r"+name;
    }

    @Override
    public ItemStack getItemStack() {
        return format(material, name);
    }

    @Override
    public void onClick(UHCPlayer uhcPlayer) {
    }

    @Override
    public ItemStack format(Material material, String name) {
        return new ItemBuilder(material).setName(name).setUnbreakable().toItemStack();
    }

    @Override
    public boolean isItemStack(ItemStack item) {
        return (item.getType() == material) && (item.getItemMeta().getDisplayName().equals(name));
    }
}
