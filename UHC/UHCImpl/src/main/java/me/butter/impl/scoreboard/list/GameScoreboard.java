package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.GraphicUtils;
import me.butter.impl.scoreboard.AbstractScoreboard;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class GameScoreboard extends AbstractScoreboard {
    public GameScoreboard(Scoreboard scoreboard) {
        super(scoreboard, "In Game", Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                new DuplicateString("", 0).getString(),
                "Timer: " + GraphicUtils.convertToAccurateTime(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()),
                new DuplicateString("", 1).getString(),
                "Players: " + UHCAPI.getInstance().getPlayerHandler().getPlayersInGame() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers(),
                "§cGroupes de " + UHCAPI.getInstance().getGameHandler().getGameConfig().getGroupSize(),
                new DuplicateString("", 2).getString(),
                new DuplicateString("§7§m---------------------", 1).getString()
        ));
    }

    @Override
    public void update() {
        setLine(2, "Timer: " + GraphicUtils.convertToAccurateTime(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()));
        setLine(4, "Players: " + UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers());
        setLine(5, "§cGroupes de " + UHCAPI.getInstance().getGameHandler().getGameConfig().getGroupSize());
        super.update();
    }
}
