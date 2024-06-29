package me.butter.api.module.power;

import me.butter.api.player.UHCPlayer;
import org.bukkit.Material;
import org.bukkit.event.block.Action;

public abstract class RightClickItemPower extends ItemPower {

    public RightClickItemPower(String name, Material material, int cooldown, int maxUses) {
        super(name, material, cooldown, maxUses);
    }

    @Override
    public void onUsePower(UHCPlayer player, Action clickAction) {
        if(clickAction == Action.LEFT_CLICK_AIR || clickAction == Action.LEFT_CLICK_BLOCK) return;
        super.onUsePower(player, clickAction);
    }
}
