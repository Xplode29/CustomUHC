package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.api.scoreboard.AbstractScoreboard;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class LobbyScoreboard extends AbstractScoreboard {
    public LobbyScoreboard(Scoreboard scoreboard) {
        super(scoreboard, "Lobby", Arrays.asList(
                new DuplicateString("§l§m----------------", 0).getString(),
                new DuplicateString("", 0).getString(),
                "Host: " + (UHCAPI.get().getGameHandler().getGameConfig().getHost() == null ? "§cNon défini" : UHCAPI.get().getGameHandler().getGameConfig().getHost().getName()),
                "Players: " + UHCAPI.get().getPlayerHandler().getPlayersInLobby().size() + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers(),
                new DuplicateString("", 1).getString(),
                new DuplicateString("§l§m----------------", 1).getString()
        ));
    }

    @Override
    public void update() {
        setLine(2, "Host: " + (UHCAPI.get().getGameHandler().getGameConfig().getHost() == null ? "§cNon défini" : UHCAPI.get().getGameHandler().getGameConfig().getHost().getName()));
        setLine(3, "Players: " + UHCAPI.get().getPlayerHandler().getPlayersInLobby().size() + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers());
        super.update();
    }
}
