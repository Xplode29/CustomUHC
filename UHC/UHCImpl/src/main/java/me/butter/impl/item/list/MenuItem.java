package me.butter.impl.item.list;

import me.butter.api.player.UHCPlayer;
import me.butter.impl.item.AbstractItem;
import org.bukkit.Material;

public class MenuItem extends AbstractItem {
    public MenuItem() {
        super(Material.NETHER_STAR, "Menu");
    }

    @Override
    public void onClick(UHCPlayer uhcPlayer) {
        uhcPlayer.sendMessage("test");
    }
}
