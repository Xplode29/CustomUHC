package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.tab.list.MainTab;
import me.butter.impl.task.TeleportingTask;
import org.bukkit.Location;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LateCommand extends AbstractCommand {
    public LateCommand() {
        super("late");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 2) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h late <joueur>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null || target.getPlayer() == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        if(target.isDisconnected()) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + target.getName() + " n'est pas connecté !"));
            return;
        }

        double alpha = Math.toRadians(new Random().nextInt(360));
        double x = UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() / 1.5D * Math.cos(alpha);
        double z = UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() / 1.5D * Math.sin(alpha);
        Location randomLoc = new Location(UHCAPI.getInstance().getWorldHandler().getWorld(),
                x,
                UHCAPI.getInstance().getWorldHandler().getWorld().getHighestBlockYAt((int) x, (int) z) + 1,
                z
        );
        target.setSpawnLocation(randomLoc);

        TeleportingTask.setPlayerInGame(target);

        target.clearEffects();

        UHCAPI.getInstance().getTabHandler().setPlayerTab(MainTab.class, target);
        UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(GameScoreboard.class, target);

        target.setPlayerState(PlayerState.IN_GAME);
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        return UHCAPI.getInstance().getPlayerHandler().getAllPlayers().stream()
                .filter(uhcPlayer -> uhcPlayer.getPlayerState() == PlayerState.IN_SPEC)
                .map(UHCPlayer::getName).collect(Collectors.toList());
    }
}
