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
                new DuplicateString("§l§m----------------", 0).getString(),
                new DuplicateString("", 0).getString(),
                "Host: ",
                "Players: " + UHCAPI.get().getPlayerHandler().getPlayersInGame() + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers(),
                "Timer: " + StringUtils.convertToAccurateTime(UHCAPI.get().getGameHandler().getGameConfig().getTimer()),
                "Kills: 0",
                new DuplicateString("", 1).getString(),
                new DuplicateString("§l§m----------------", 1).getString()
        ));
    }

    @Override
    public void update() {
        setLine(2, "Host: " + (UHCAPI.get().getGameHandler().getGameConfig().getHost() == null ? "§cNon défini" : UHCAPI.get().getGameHandler().getGameConfig().getHost().getName()));
        setLine(3, "Players: " + UHCAPI.get().getPlayerHandler().getPlayersInGame().size() + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers());
        setLine(4, "Timer: " + StringUtils.convertToAccurateTime(UHCAPI.get().getGameHandler().getGameConfig().getTimer()));
        super.update();
    }

    @Override
    public void updatePlayer(UHCPlayer uhcPlayer) {
        setLine(5, "Kills: " + uhcPlayer.getKilledPlayers().size());
        super.updatePlayer(uhcPlayer);
    }
}
