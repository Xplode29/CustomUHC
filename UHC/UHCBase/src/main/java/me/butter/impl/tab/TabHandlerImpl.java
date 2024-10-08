package me.butter.impl.tab;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.tab.CustomTab;
import me.butter.api.tab.TabHandler;
import me.butter.impl.tab.list.MainTab;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TabHandlerImpl implements TabHandler {

    private List<CustomTab> customTabList;

    public TabHandlerImpl() {
        customTabList = new ArrayList<>();

        addTab(new MainTab());

        new BukkitRunnable() {
            @Override
            public void run() {
                updateAllTabs();
            }
        }.runTaskTimer(UHCAPI.getInstance(), 0, 20);
    }

    @Override
    public void updateAllTabs() {
        customTabList.forEach(CustomTab::updateTab);
    }

    @Override
    public void addTab(CustomTab tab) {
        if(customTabList.contains(tab)) return;
        customTabList.add(new MainTab());
    }

    @Override
    public void setPlayerTab(Class<? extends CustomTab> tabClass, UHCPlayer uhcPlayer) {
        CustomTab customTab = getTab(tabClass);
        if (customTab != null) {
            removePlayerTab(uhcPlayer);
            customTab.addPlayer(uhcPlayer);
        }
    }

    @Override
    public void removePlayerTab(UHCPlayer uhcPlayer) {
        customTabList.forEach(tab -> tab.removePlayer(uhcPlayer));
    }

    @Override
    public CustomTab getTab(Class<? extends CustomTab> tabClass) {
        return customTabList.stream().filter(tab -> tab.getClass() == tabClass).findFirst().orElse(null);
    }
}
