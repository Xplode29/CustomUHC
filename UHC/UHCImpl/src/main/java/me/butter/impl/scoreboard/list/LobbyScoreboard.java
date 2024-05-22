package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.impl.scoreboard.AbstractScoreboard;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class LobbyScoreboard extends AbstractScoreboard {
    public LobbyScoreboard(Scoreboard scoreboard) {
        super(scoreboard, UHCAPI.getInstance().getModuleHandler().hasModule() ? "§7" + UHCAPI.getInstance().getModuleHandler().getModule().getName() : "§7UHC", Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                new DuplicateString("", 0).getString(),
                "Host: " + (UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "§cNon défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()),
                new DuplicateString("", 1).getString(),
                "Players: " + (UHCAPI.getInstance().getPlayerHandler().getPlayers().size() > UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                        UHCAPI.getInstance().getPlayerHandler().getPlayers().size() + "/" + ChatColor.GREEN + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers(),
                "Status: " + UHCAPI.getInstance().getGameHandler().getGameState().getName(),
                new DuplicateString("", 2).getString(),
                new DuplicateString("§7§m---------------------", 1).getString()
        ));
    }

    @Override
    public void update() {
        setLine(2, "Host: " + (UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "§cNon défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()));
        setLine(4, "Players: " + (UHCAPI.getInstance().getPlayerHandler().getPlayers().size() > UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                UHCAPI.getInstance().getPlayerHandler().getPlayers().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers());
        setLine(5, "Status: " + UHCAPI.getInstance().getGameHandler().getGameState().getName());
        super.update();
    }
}
