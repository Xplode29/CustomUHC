package me.butter.api.item;

import me.butter.api.player.UHCPlayer;
import org.bukkit.entity.Player;

public interface ItemHandler {

    CustomItem getCustomItem(Class<? extends CustomItem> itemClass);

    void addCustomItem(CustomItem item);

    void giveItemToPlayer(Class<? extends CustomItem> itemClass, UHCPlayer uhcPlayer);
}
