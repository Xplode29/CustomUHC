package me.butter.api.scoreboard;

import me.butter.api.player.UHCPlayer;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public interface ScoreboardHandler {

    void setPlayerScoreboard(Class<? extends CustomScoreboard> scoreboardClass, UHCPlayer uhcPlayer);

    void removePlayerScoreboard(UHCPlayer uhcPlayer);

    void updatePlayerScoreboard(UHCPlayer uhcPlayer);

    void addScoreboard(CustomScoreboard scoreboard);

    Scoreboard getNewScoreboard();

    List<CustomScoreboard> getScoreboards();
}
