package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.scoreboard.AbstractScoreboard;
import me.butter.api.utils.StringUtils;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class GameScoreboard extends AbstractScoreboard {
    public GameScoreboard(Scoreboard scoreboard) {
        super(scoreboard, "In Game", Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                new DuplicateString("", 0).getString(),
                "Timer: " + StringUtils.convertToAccurateTime(UHCAPI.get().getGameHandler().getGameConfig().getTimer()),
                new DuplicateString("", 1).getString(),
                "Players: " + UHCAPI.get().getPlayerHandler().getPlayersInGame() + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers(),
                "§cGroupes de " + UHCAPI.get().getGameHandler().getGameConfig().getGroupSize(),
                new DuplicateString("", 2).getString(),
                new DuplicateString("§7§m---------------------", 1).getString()
        ));
    }

    @Override
    public void update() {
        setLine(2, "Timer: " + StringUtils.convertToAccurateTime(UHCAPI.get().getGameHandler().getGameConfig().getTimer()));
        setLine(4, "Players: " + UHCAPI.get().getPlayerHandler().getPlayersInGame().size() + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers());
        setLine(5, "§cGroupes de " + UHCAPI.get().getGameHandler().getGameConfig().getGroupSize());
        super.update();
    }
}
