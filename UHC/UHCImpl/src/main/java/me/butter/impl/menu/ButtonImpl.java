package me.butter.impl.menu;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public abstract class ButtonImpl implements Button {

    ItemStack icon;

    public ButtonImpl() {}

    @Override
    public ItemStack getIcon() {
        return icon;
    }

    @Override
    public void setIcon(ItemStack itemStack) {
        this.icon = itemStack;
    }

    @Override
    public boolean doesUpdateGui() {
        return false;
    }

    @Override
    public void onClick(UHCPlayer player, ClickType clickType) {

    }
}
