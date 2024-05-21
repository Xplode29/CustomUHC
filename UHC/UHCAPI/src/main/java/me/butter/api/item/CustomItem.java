package me.butter.api.item;

import me.butter.api.player.UHCPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface CustomItem {

    ItemStack getItemStack();

    void onClick(UHCPlayer uhcPlayer);

    ItemStack format(Material material, String name);

    boolean isItemStack(ItemStack item);
}
