package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.scoreboard.AbstractScoreboard;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class GameScoreboard extends AbstractScoreboard {

    boolean changed = false;

    public GameScoreboard(Scoreboard scoreboard) {
        super(scoreboard, "§lUHC", Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                ChatUtils.formatScoreboard("Host", UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "Non défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()),
                ChatUtils.formatScoreboard("Joueurs", UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers()),
                ChatUtils.formatScoreboard("Groupes", UHCAPI.getInstance().getGameHandler().getGameConfig().getGroupSize() + ""),
                new DuplicateString("§7§m---------------------", 1).getString(),
                ChatUtils.formatScoreboard("Episode", String.valueOf(UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisode())),
                ChatUtils.formatScoreboard("Timer", GraphicUtils.convertToAccurateTime(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer())),
                ChatUtils.formatScoreboard("PVP", UHCAPI.getInstance().getGameHandler().getGameConfig().isPVP()),
                new DuplicateString("§7§m---------------------", 2).getString(),
                ChatUtils.formatScoreboard("Document", "/doc")
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
                ChatUtils.formatScoreboard("Joueurs", UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers()),
                ChatUtils.formatScoreboard("Groupes", UHCAPI.getInstance().getGameHandler().getGameConfig().getGroupSize() + ""),
                new DuplicateString("§7§m---------------------", 1).getString(),
                ChatUtils.formatScoreboard("Episode", String.valueOf(UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisode())),
                ChatUtils.formatScoreboard("Timer", GraphicUtils.convertToAccurateTime(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer())),
                ChatUtils.formatScoreboard("PVP", UHCAPI.getInstance().getGameHandler().getGameConfig().isPVP()),
                new DuplicateString("§7§m---------------------", 2).getString(),
                ChatUtils.formatScoreboard("Document", "/doc")
        ));
        super.update();
    }
}
