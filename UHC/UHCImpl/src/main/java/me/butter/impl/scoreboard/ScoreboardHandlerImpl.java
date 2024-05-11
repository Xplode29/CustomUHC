package me.butter.impl.scoreboard;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.scoreboard.AbstractScoreboard;
import me.butter.api.scoreboard.ScoreboardHandler;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.scoreboard.list.LobbyScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class ScoreboardHandlerImpl implements ScoreboardHandler {

    List<AbstractScoreboard> scoreboardList;
    ScoreboardManager scoreboardManager;

    public ScoreboardHandlerImpl() {
        scoreboardList = Lists.newArrayList();
        scoreboardManager = Bukkit.getScoreboardManager();

        scoreboardList.add(new LobbyScoreboard(scoreboardManager.getNewScoreboard()));
        scoreboardList.add(new GameScoreboard(scoreboardManager.getNewScoreboard()));

        new BukkitRunnable() {

            @Override
            public void run() {
                for(AbstractScoreboard scoreboard : scoreboardList) {
                    scoreboard.update();
                }
            }
        }.runTaskTimer(UHCAPI.get(), 0, 20);
    }

    @Override
    public void setPlayerScoreboard(Class<? extends AbstractScoreboard> scoreboardClass, UHCPlayer uhcPlayer) {
        for(AbstractScoreboard scoreboard : scoreboardList) {
            if (scoreboard.getPlayers().contains(uhcPlayer.getUniqueId())) {
                scoreboard.removePlayer(uhcPlayer);
            }
        }

        for(AbstractScoreboard scoreboard : scoreboardList) {
            if(scoreboard.getClass() == scoreboardClass) {
                scoreboard.addPlayer(uhcPlayer);
                scoreboard.updatePlayer(uhcPlayer);
                return;
            }
        }
    }

    @Override
    public void removePlayerScoreboard(UHCPlayer uhcPlayer) {
        for(AbstractScoreboard scoreboard : scoreboardList) {
            if(scoreboard.getPlayers().contains(uhcPlayer.getUniqueId())) {
                scoreboard.removePlayer(uhcPlayer);
                return;
            }
        }
    }

    @Override
    public void updatePlayerScoreboard(UHCPlayer uhcPlayer) {
        for(AbstractScoreboard scoreboard : scoreboardList) {
            if(scoreboard.getPlayers().contains(uhcPlayer.getUniqueId())) {
                scoreboard.update();
            }
            return;
        }
    }

    @Override
    public List<AbstractScoreboard> getScoreboards() {
        return scoreboardList;
    }
}
