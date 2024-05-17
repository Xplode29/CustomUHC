package me.butter.impl.scoreboard;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.scoreboard.CustomScoreboard;
import me.butter.api.scoreboard.ScoreboardHandler;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.scoreboard.list.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class ScoreboardHandlerImpl implements ScoreboardHandler {

    List<CustomScoreboard> scoreboardList;
    ScoreboardManager scoreboardManager;

    public ScoreboardHandlerImpl() {
        scoreboardList = Lists.newArrayList();
        scoreboardManager = Bukkit.getScoreboardManager();

        scoreboardList.add(new LobbyScoreboard(scoreboardManager.getNewScoreboard()));
        scoreboardList.add(new GameScoreboard(scoreboardManager.getNewScoreboard()));

        new BukkitRunnable() {
            @Override
            public void run() {
                for(CustomScoreboard scoreboard : scoreboardList) {
                    scoreboard.update();
                }
            }
        }.runTaskTimer(UHCAPI.get(), 0, 20);
    }

    @Override
    public void setPlayerScoreboard(Class<? extends CustomScoreboard> scoreboardClass, UHCPlayer uhcPlayer) {
        for(CustomScoreboard scoreboard : scoreboardList) {
            if (scoreboard.getPlayers().contains(uhcPlayer.getUniqueId())) {
                scoreboard.removePlayer(uhcPlayer);
            }
        }

        for(CustomScoreboard scoreboard : scoreboardList) {
            if(scoreboard.getClass() == scoreboardClass) {
                scoreboard.addPlayer(uhcPlayer);
                scoreboard.updatePlayer(uhcPlayer);
                return;
            }
        }
    }

    @Override
    public void removePlayerScoreboard(UHCPlayer uhcPlayer) {
        for(CustomScoreboard scoreboard : scoreboardList) {
            if(scoreboard.getPlayers().contains(uhcPlayer.getUniqueId())) {
                scoreboard.removePlayer(uhcPlayer);
                return;
            }
        }
    }

    @Override
    public void updatePlayerScoreboard(UHCPlayer uhcPlayer) {
        for(CustomScoreboard scoreboard : scoreboardList) {
            if(scoreboard.getPlayers().contains(uhcPlayer.getUniqueId())) {
                scoreboard.update();
            }
            return;
        }
    }

    @Override
    public List<CustomScoreboard> getScoreboards() {
        return scoreboardList;
    }
}
