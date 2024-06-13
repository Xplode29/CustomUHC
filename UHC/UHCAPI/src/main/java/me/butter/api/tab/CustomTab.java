package me.butter.api.tab;

import me.butter.api.player.UHCPlayer;

import java.util.List;
import java.util.UUID;

public interface CustomTab {

    //void setHeaderLine(UUID playerId, int line, List<String> lineUpdates);

    //void setFooterLine(UUID playerId, int line, List<String> lineUpdates);

    void addPlayer(UHCPlayer uhcPlayer); void removePlayer(UHCPlayer uhcPlayer);

    void updateTab();

    List<String> modifyHeader(UHCPlayer uhcPlayer, List<String> header); List<String> modifyFooter(UHCPlayer uhcPlayer, List<String> footer);
}
