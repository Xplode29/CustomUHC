package me.butter.impl.task;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class SpawnPointsTask extends BukkitRunnable {

    private final List<UHCPlayer> players;

    private int spawnPoints;

    public SpawnPointsTask() {
        this.players = Lists.newArrayList(UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby());
        this.spawnPoints = 0;
        UHCAPI.getInstance().getGameHandler().setGameState(GameState.TELEPORTING);
        this.runTaskTimer(UHCImpl.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if (this.players.isEmpty()) {
            this.cancel();
            Bukkit.getScheduler().runTaskLater(UHCImpl.getInstance(), TeleportingTask::new, 20);
            return;
        }

        UHCPlayer uhcPlayer = this.players.get(0);

        int randomX = new Random().nextInt(UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() * 2 - 20) - UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() + 20;
        int randomZ = new Random().nextInt(UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() * 2 - 20) - UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() + 20;

        Location randomLoc = new Location(
                UHCAPI.getInstance().getWorldHandler().getWorld(),
                randomX,
                UHCAPI.getInstance().getWorldHandler().getWorld().getHighestBlockYAt(randomX, randomZ) + 1,
                randomZ
        );

        uhcPlayer.setSpawnLocation(randomLoc);

        if(!uhcPlayer.getSpawnLocation().getChunk().isLoaded()) uhcPlayer.getSpawnLocation().getChunk().load();

        spawnPoints += 1;
        UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().forEach(uhcPlayer1 -> uhcPlayer1.sendActionBar(
                "Spawns: " + spawnPoints + "/" + UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size()
        ));

        UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().forEach(uhcPlayer1 -> uhcPlayer1.getPlayer().playSound(
                uhcPlayer1.getLocation(), Sound.NOTE_STICKS, 6.0F, 1.0F
        ));

        this.players.remove(0);
    }
}
