package me.butter.impl.scoreboard.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.scoreboard.AbstractScoreboard;
import me.butter.impl.scoreboard.DuplicateString;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;

public class GameScoreboard extends AbstractScoreboard {
    public GameScoreboard(Scoreboard scoreboard) {
        super(scoreboard, UHCAPI.getInstance().getModuleHandler().hasModule() ? "§l" + UHCAPI.getInstance().getModuleHandler().getModule().getName() : "§lUHC", Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                ChatUtils.formatScoreboard("Host", UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "Non défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()),
                ChatUtils.formatScoreboard("Joueurs", UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers()),
                ChatUtils.formatScoreboard("Groupes", UHCAPI.getInstance().getGameHandler().getGameConfig().getGroupSize() + ""),
                new DuplicateString("§7§m---------------------", 1).getString(),
                ChatUtils.formatScoreboard("Timer", GraphicUtils.convertToAccurateTime(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer())),
                ChatUtils.formatScoreboard("PVP", UHCAPI.getInstance().getGameHandler().getGameConfig().isPVP()),
                ChatUtils.formatScoreboard("Meetup", UHCAPI.getInstance().getGameHandler().getGameConfig().isMeetup()),
                new DuplicateString("§7§m---------------------", 2).getString(),
                ChatUtils.formatScoreboard("Bordure", String.valueOf(UHCAPI.getInstance().getWorldHandler().getWorld() == null ? 0 : (int) (UHCAPI.getInstance().getWorldHandler().getWorld().getWorldBorder().getSize() / 2))),
                ChatUtils.formatScoreboard("Episode", String.valueOf(UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisode())),
                new DuplicateString("§7§m---------------------", 3).getString()

        ));
    }

    @Override
    public void update() {
        setLines(Arrays.asList(
                new DuplicateString("§7§m---------------------", 0).getString(),
                ChatUtils.formatScoreboard("Host", UHCAPI.getInstance().getGameHandler().getGameConfig().getHost() == null ? "Non défini" : UHCAPI.getInstance().getGameHandler().getGameConfig().getHost().getName()),
                ChatUtils.formatScoreboard("Joueurs", UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers()),
                ChatUtils.formatScoreboard("Groupes", UHCAPI.getInstance().getGameHandler().getGameConfig().getGroupSize() + ""),
                new DuplicateString("§7§m---------------------", 1).getString(),
                ChatUtils.formatScoreboard("Timer", GraphicUtils.convertToAccurateTime(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer())),
                ChatUtils.formatScoreboard("PVP", UHCAPI.getInstance().getGameHandler().getGameConfig().isPVP()),
                ChatUtils.formatScoreboard("Meetup", UHCAPI.getInstance().getGameHandler().getGameConfig().isMeetup()),
                new DuplicateString("§7§m---------------------", 2).getString(),
                ChatUtils.formatScoreboard("Bordure", String.valueOf(UHCAPI.getInstance().getWorldHandler().getWorld() == null ? 0 : (int) (UHCAPI.getInstance().getWorldHandler().getWorld().getWorldBorder().getSize() / 2))),
                ChatUtils.formatScoreboard("Episode", String.valueOf(UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisode())),
                new DuplicateString("§7§m---------------------", 3).getString()
        ));
        super.update();
    }
}
