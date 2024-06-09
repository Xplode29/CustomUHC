package me.butter.ninjago.items;

import me.butter.api.item.CustomItem;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.item.AbstractItem;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AbstractGoldenWeapon extends AbstractItem {

    UHCPlayer holder;

    public AbstractGoldenWeapon(Material material, String name) {
        super(material, name);
    }

    public UHCPlayer getHolder() {
        return holder;
    }

    public void setHolder(UHCPlayer holder) {
        this.holder = holder;
    }
}
