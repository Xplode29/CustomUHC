package me.butter.api.menu;

import me.butter.api.player.UHCPlayer;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public interface Menu {
    void open(Menu previousMenu, boolean isPrevMenu); void closeMenu();

    void update();

    void onClick(int slot, UHCPlayer uhcPlayer, ClickType clickType);

    Inventory getInventory();

    Map<Integer, Button> getButtons(); void setButtons(Map<Integer, Button> buttons);

    UHCPlayer getOpener(); void setOpener(UHCPlayer opener);

    boolean doesUpdateOnClick(); void setUpdateOnClick(boolean updateOnClick);
}
