package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.scoreboard.AbstractScoreboard;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class LobbyScoreboard extends AbstractScoreboard {
    public LobbyScoreboard(Scoreboard scoreboard) {
        super(scoreboard, UHCAPI.getInstance().getModuleHandler().hasModule() ? "§7" + UHCAPI.getInstance().getModuleHandler().getModule().getName() : "§7UHC", Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                ChatUtils.formatScoreboard("Host", UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "Non défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()),
                ChatUtils.formatScoreboard("Joueurs", UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers()),
                ChatUtils.formatScoreboard("Status", UHCAPI.getInstance().getGameHandler().getGameState().getName()),
                new DuplicateString("§7§m---------------------", 1).getString(),
                ChatUtils.formatScoreboard("Document", "/doc")
        ));
    }

    @Override
    public void update() {
        setLines(Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                ChatUtils.formatScoreboard("Host", UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "Non défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()),
                ChatUtils.formatScoreboard("Joueurs", UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers()),
                ChatUtils.formatScoreboard("Status", UHCAPI.getInstance().getGameHandler().getGameState().getName()),
                new DuplicateString("§7§m---------------------", 1).getString(),
                ChatUtils.formatScoreboard("Document", "/doc")
        ));
        super.update();
    }
}
