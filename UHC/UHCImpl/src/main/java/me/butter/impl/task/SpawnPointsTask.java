package me.butter.impl.task;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.BlockUtils;
import me.butter.impl.UHCImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class SpawnPointsTask extends BukkitRunnable {

    private final List<UHCPlayer> players;

    private int spawnPoints;

    public SpawnPointsTask() {
        this.players = Lists.newArrayList(UHCAPI.get().getPlayerHandler().getPlayersInLobby());
        this.spawnPoints = 0;
        UHCAPI.get().getGameHandler().setGameState(GameState.TELEPORTING);
        this.runTaskTimer(UHCImpl.get(), 0, 5);
    }

    @Override
    public void run() {
        if (this.players.isEmpty()) {
            this.cancel();
            Bukkit.getScheduler().runTaskLater(UHCImpl.get(), TeleportingTask::new, 20);
            return;
        }

        UHCPlayer uhcPlayer = this.players.get(0);

        Location randomLoc = new Location(
                UHCAPI.get().getWorldHandler().getWorld(),
                new Random().nextInt(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() - 20) - UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() / 2 + 20,
                200,
                new Random().nextInt(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() - 20) - UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() / 2 + 20
        );

        UHCAPI.get().getPlayerHandler().getUHCPlayer(uhcPlayer.getUniqueId()).setSpawnLocation(randomLoc);

        int platformSize = 3;
        BlockUtils.fillBlocks(uhcPlayer.getSpawnLocation().getWorld(), uhcPlayer.getSpawnLocation().getBlockX() - platformSize/2, uhcPlayer.getSpawnLocation().getBlockY() - 1, uhcPlayer.getSpawnLocation().getBlockZ() - platformSize/2, platformSize, 1, platformSize, Material.GLASS);


        spawnPoints += 1;
        UHCAPI.get().getPlayerHandler().getPlayersInLobby().forEach(uhcPlayer1 -> uhcPlayer1.sendActionBar(
                "§aGénération des points d'apparitions §7(" + spawnPoints + "/" + UHCAPI.get().getPlayerHandler().getPlayersInLobby().size() + ")"
        ));

        this.players.remove(0);
    }
}
