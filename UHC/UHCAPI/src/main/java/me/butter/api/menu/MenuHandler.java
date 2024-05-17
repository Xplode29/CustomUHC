package me.butter.api.menu;

import me.butter.api.player.UHCPlayer;

import java.util.Map;
import java.util.UUID;

public interface MenuHandler {

    Map<UUID, Menu> getOpenedMenus();

    Menu getOpenedMenu(UHCPlayer uhcPlayer);

    void openMenu(UHCPlayer uhcPlayer, Menu menu, boolean isPrevMenu);
    void closeMenu(UHCPlayer uhcPlayer);
}
