package me.butter.api.item;

import me.butter.api.player.UHCPlayer;

import java.util.List;

public interface ItemHandler {

    List<CustomItem> getCustomItems();
    CustomItem getCustomItem(Class<? extends CustomItem> itemClass);

    void addCustomItem(CustomItem item);

    void giveItemToPlayer(Class<? extends CustomItem> itemClass, UHCPlayer uhcPlayer);
    void giveItemToPlayer(Class<? extends CustomItem> itemClass, int slot, UHCPlayer uhcPlayer);

    void removeItemFromPlayer(Class<? extends CustomItem> itemClass, UHCPlayer uhcPlayer);

    void giveLobbyItems(UHCPlayer uhcPlayer); void removeLobbyItems(UHCPlayer uhcPlayer);
}
