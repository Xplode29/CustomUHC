package me.butter.api.menu;

import me.butter.api.player.UHCPlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public interface Button {

    ItemStack getIcon(); void setIcon(ItemStack itemStack);

    boolean doesUpdateGui();

    void onClick(UHCPlayer player, ClickType clickType);
}
