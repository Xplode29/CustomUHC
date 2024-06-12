package me.butter.api.tab;

import me.butter.api.player.UHCPlayer;

public interface TabHandler {

    void updateAllTabs();

    void addTab(CustomTab tab);

    void setPlayerTab(Class<? extends CustomTab> tabClass, UHCPlayer uhcPlayer);
    void removePlayerTab(UHCPlayer uhcPlayer);

    CustomTab getTab(Class<? extends CustomTab> tabClass);
}
