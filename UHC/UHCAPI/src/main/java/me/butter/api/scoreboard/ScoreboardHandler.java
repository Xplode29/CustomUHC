package me.butter.api.scoreboard;

import me.butter.api.player.UHCPlayer;

import java.util.List;

public interface ScoreboardHandler {

    void setPlayerScoreboard(Class<? extends AbstractScoreboard> scoreboardClass, UHCPlayer uhcPlayer);

    void removePlayerScoreboard(UHCPlayer uhcPlayer);

    void updatePlayerScoreboard(UHCPlayer uhcPlayer);

    List<AbstractScoreboard> getScoreboards();
}
