package me.butter.impl.task;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TeleportingTask extends BukkitRunnable {

    private final List<UHCPlayer> players;

    private int playerTeleported;

    public TeleportingTask() {
        this.players = Lists.newArrayList(UHCAPI.get().getPlayerHandler().getPlayersInLobby());
        this.playerTeleported = 0;
        this.runTaskTimer(UHCAPI.get(), 0, 5);
    }

    @Override
    public void run() {
        if (this.players.isEmpty()) {
            this.cancel();
            Bukkit.getScheduler().runTaskLater(UHCImpl.get(), StartTask::new,20 * 5);
            return;
        }

        UHCPlayer uhcPlayer = this.players.get(0);

        Location teleport = uhcPlayer.getSpawnLocation().clone().add(0, 3, 0);

        if (uhcPlayer.getPlayer() != null)
            uhcPlayer.getPlayer().teleport(teleport);

        playerTeleported += 1;
        UHCAPI.get().getPlayerHandler().getPlayersInLobby().forEach(uhcPlayer1 -> uhcPlayer1.sendActionBar(
                "§aTéléportation de " + uhcPlayer.getName() + " §7(" + playerTeleported + "/" + UHCAPI.get().getPlayerHandler().getPlayersInLobby().size() + ")"
        ));

        this.players.remove(0);
    }
}
