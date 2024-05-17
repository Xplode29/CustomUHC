package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.impl.scoreboard.AbstractScoreboard;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class LobbyScoreboard extends AbstractScoreboard {
    public LobbyScoreboard(Scoreboard scoreboard) {
        super(scoreboard, "Lobby", Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                new DuplicateString("", 0).getString(),
                "Host: " + (UHCAPI.get().getGameHandler().getGameConfig().getHost() == null ? "§cNon défini" : UHCAPI.get().getGameHandler().getGameConfig().getHost().getName()),
                new DuplicateString("", 1).getString(),
                "Players: " + (UHCAPI.get().getPlayerHandler().getPlayers().size() > UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                        UHCAPI.get().getPlayerHandler().getPlayers().size() + "/" + ChatColor.GREEN + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers(),
                "Status: " + UHCAPI.get().getGameHandler().getGameState().getName(),
                new DuplicateString("", 2).getString(),
                new DuplicateString("§7§m---------------------", 1).getString()
        ));
    }

    @Override
    public void update() {
        setLine(2, "Host: " + (UHCAPI.get().getGameHandler().getGameConfig().getHost() == null ? "§cNon défini" : UHCAPI.get().getGameHandler().getGameConfig().getHost().getName()));
        setLine(4, "Players: " + (UHCAPI.get().getPlayerHandler().getPlayers().size() > UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                UHCAPI.get().getPlayerHandler().getPlayers().size() + "/" + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers());
        setLine(5, "Status: " + UHCAPI.get().getGameHandler().getGameState().getName());
        super.update();
    }
}
