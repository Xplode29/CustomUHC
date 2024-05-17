package me.butter.api.scoreboard;

import me.butter.api.player.UHCPlayer;

import java.util.List;
import java.util.UUID;

public interface CustomScoreboard {
    void addPlayer(UHCPlayer uhcPlayer); void removePlayer(UHCPlayer uhcPlayer);

    List<UUID> getPlayers();

    void update(); void updatePlayer(UHCPlayer uhcPlayer);

    void setTitle(String title); void setLine(int line, String text); void setLines(List<String> newLines);
}