package me.butter.api.module.power;

import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public abstract class RightClickItemPower extends ItemPower {

    public RightClickItemPower(String name, Material material, int cooldown, int maxUses) {
        super(name, material, cooldown, maxUses);
    }

    @Override
    public void onUsePower(UHCPlayer player, Action clickAction) {
        if(clickAction == Action.LEFT_CLICK_AIR || clickAction == Action.LEFT_CLICK_BLOCK) return;
        super.onUsePower(player, clickAction);
    }

    @Override
    public ItemStack getItem() {
        return new ItemBuilder(getMaterial()).setName("§l" + getName() + "§r - §lClick Droit§r").toItemStack();
    }
}
