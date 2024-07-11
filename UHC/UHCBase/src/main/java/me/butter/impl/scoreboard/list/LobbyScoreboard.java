package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.scoreboard.AbstractScoreboard;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class LobbyScoreboard extends AbstractScoreboard {

    boolean changed = false;

    public LobbyScoreboard(Scoreboard scoreboard) {
        super(scoreboard, UHCAPI.getInstance().getModuleHandler().hasModule() ? "§l" + UHCAPI.getInstance().getModuleHandler().getModule().getName() : "§lUHC", Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                ChatUtils.formatScoreboard("Host", UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "Non défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()),
                ChatUtils.formatScoreboard("Joueurs", UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers()),
                ChatUtils.formatScoreboard("Status", UHCAPI.getInstance().getGameHandler().getGameState().getName()),
                new DuplicateString("§7§m---------------------", 1).getString(),
                ChatUtils.formatScoreboard("Document", "/n doc")
        ));
    }

    @Override
    public void update() {
        if(UHCAPI.getInstance().getModuleHandler().hasModule() && !changed) {
            setTitle("§l" + UHCAPI.getInstance().getModuleHandler().getModule().getName());
            changed = true;
        }
        setLines(Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                ChatUtils.formatScoreboard("Host", UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "Non défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()),
                ChatUtils.formatScoreboard("Joueurs", UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers()),
                ChatUtils.formatScoreboard("Status", UHCAPI.getInstance().getGameHandler().getGameState().getName()),
                new DuplicateString("§7§m---------------------", 1).getString(),
                ChatUtils.formatScoreboard("Document", "/n doc")
        ));
        super.update();
    }
}
