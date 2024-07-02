package me.butter.impl.scoreboard;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.scoreboard.CustomScoreboard;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractScoreboard implements CustomScoreboard {

    private final Scoreboard scoreboard;
    private final Objective objective;

    private List<String> lines;

    private List<UUID> players;

    public AbstractScoreboard(Scoreboard scoreboard, String title, List<String> lines) {
        this.scoreboard = scoreboard;

        this.objective = scoreboard.registerNewObjective("§l" + title, "dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.lines = new ArrayList<>();
        this.players = new ArrayList<>();

        setLines(lines);
    }

    public void addPlayer(UHCPlayer uhcPlayer) {
        if(!players.contains(uhcPlayer.getUniqueId())) {
            this.players.add(uhcPlayer.getUniqueId());
        }
    }

    public void removePlayer(UHCPlayer uhcPlayer) {
        players.remove(uhcPlayer.getUniqueId());
    }


    public List<UUID> getPlayers() {
        return players;
    }

    public void update() {
        for(UUID uuid : players) {
            if(UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(uuid) != null) {
                updatePlayer(UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(uuid));
            }
        }
    }

    public void updatePlayer(UHCPlayer uhcPlayer) {
        if(uhcPlayer.getPlayer() != null) {
            uhcPlayer.getPlayer().setScoreboard(scoreboard);
        }
    }

    public void setTitle(String title) {
        objective.setDisplayName(title);
    }

    public void setLine(int line, String text) {
        if(lines.size() <= line) {
            for(int i = lines.size(); i <= line; i++) {
                lines.add("");
                objective.getScore(lines.get(i)).setScore(lines.size() - i - 1);
            }
        }
        scoreboard.resetScores(this.lines.get(line));

        lines.set(line, text);
        objective.getScore(text).setScore(lines.size() - line - 1);
    }

    public void setLines(List<String> newLines) {
        for(int i = 0; i < newLines.size(); i++) {
            if(i < this.lines.size()) {
                scoreboard.resetScores(this.lines.get(i));
            }
            objective.getScore(newLines.get(i)).setScore(newLines.size() - i - 1);
        }

        this.lines = newLines;
    }

    @Override
    public Scoreboard getScoreboard() {
        return scoreboard;
    }
}
