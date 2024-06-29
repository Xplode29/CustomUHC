package me.butter.api.tab;

import me.butter.api.player.UHCPlayer;

import java.util.List;

public interface CustomTab {

    void addPlayer(UHCPlayer uhcPlayer); void removePlayer(UHCPlayer uhcPlayer);

    void updateTab();

    List<String> modifyHeader(UHCPlayer uhcPlayer, List<String> header); List<String> modifyFooter(UHCPlayer uhcPlayer, List<String> footer);
}
