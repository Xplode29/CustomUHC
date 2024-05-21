package me.butter.impl.task;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TeleportingTask extends BukkitRunnable {

    private final List<UHCPlayer> players;

    private int playerTeleported;

    public TeleportingTask() {
        this.players = Lists.newArrayList(UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby());
        this.playerTeleported = 0;
        this.runTaskTimer(UHCAPI.getInstance(), 0, 5);
    }

    @Override
    public void run() {
        if (this.players.isEmpty()) {
            this.cancel();
            Bukkit.getScheduler().runTaskLater(UHCImpl.getInstance(), StartTask::new,20 * 5);
            return;
        }

        UHCPlayer uhcPlayer = this.players.get(0);

        Location teleport = uhcPlayer.getSpawnLocation().clone().add(0.5, 3, 0.5);

        if (uhcPlayer.getPlayer() != null) {
            if(!uhcPlayer.getSpawnLocation().getChunk().isLoaded()) uhcPlayer.getSpawnLocation().getChunk().load();

            new BukkitRunnable() {

                @Override
                public void run() {
                    uhcPlayer.getPlayer().teleport(teleport);
                }
            }.runTaskLater(UHCAPI.getInstance(), 1);
        }

        playerTeleported += 1;
        UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().forEach(uhcPlayer1 -> uhcPlayer1.sendActionBar(
                "Téléportation de " + uhcPlayer.getName() + " (" + playerTeleported + "/" + UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size() + ")"
        ));

        uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_STICKS, 6.0F, 1.0F);

        this.players.remove(0);
    }
}
