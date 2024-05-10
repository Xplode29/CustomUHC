package me.butter.impl.task;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class LaunchGameTask extends BukkitRunnable {

    private int timer;

    public LaunchGameTask() {
        timer = 10;
        this.runTaskTimer(UHCImpl.get(), 0, 20);
    }

    @Override
    public void run() {
        if(!UHCAPI.get().getGameHandler().getGameConfig().isStarting()) {
            cancel();
            return;
        }

        for(UHCPlayer uhcPlayer : UHCAPI.get().getPlayerHandler().getPlayersInLobby()) {
            if(timer == 0) {
                uhcPlayer.sendTitle("Partie Lancée !", ChatColor.GREEN);
            }
            else {
                uhcPlayer.sendTitle("Début dans " + timer, ChatColor.GREEN);
            }
        }

        timer--;

        if(timer < 0) {
            cancel();
            Bukkit.getScheduler().runTaskLater(UHCImpl.get(), SpawnPointsTask::new, 20);
            return;
        }
    }
}
