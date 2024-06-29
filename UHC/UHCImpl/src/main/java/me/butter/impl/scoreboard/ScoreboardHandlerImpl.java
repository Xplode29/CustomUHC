package me.butter.impl.scoreboard;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.scoreboard.CustomScoreboard;
import me.butter.api.scoreboard.ScoreboardHandler;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.List;

public class ScoreboardHandlerImpl implements ScoreboardHandler {

    private List<CustomScoreboard> scoreboardList;
    private ScoreboardManager scoreboardManager;

    public ScoreboardHandlerImpl() {
        scoreboardList = Lists.newArrayList();
        scoreboardManager = Bukkit.getScoreboardManager();

        new BukkitRunnable() {
            @Override
            public void run() {
                for(CustomScoreboard scoreboard : scoreboardList) {
                    scoreboard.update();
                }
            }
        }.runTaskTimer(UHCAPI.getInstance(), 0, 20);
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
    public void addScoreboard(CustomScoreboard scoreboard) {
        if(!scoreboardList.contains(scoreboard))
            scoreboardList.add(scoreboard);
    }

    @Override
    public Scoreboard getNewScoreboard() {
        return scoreboardManager.getNewScoreboard();
    }


    @Override
    public List<CustomScoreboard> getScoreboards() {
        return scoreboardList;
    }
}
