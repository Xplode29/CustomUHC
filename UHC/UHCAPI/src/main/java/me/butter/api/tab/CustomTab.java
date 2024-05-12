package me.butter.api.tab;

import me.butter.api.player.UHCPlayer;

import java.util.List;

public interface CustomTab {

    void setHeaderLine(int line, String text); void setHeaderLine(int line, List<String> lineUpdates);
    void setFooterLine(int line, String text); void setFooterLine(int line, List<String> lineUpdates);

    void updateTab();

    void addPlayer(UHCPlayer uhcPlayer); void removePlayer(UHCPlayer uhcPlayer);
}
