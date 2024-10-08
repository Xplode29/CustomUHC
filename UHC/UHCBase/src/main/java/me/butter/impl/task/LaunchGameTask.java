package me.butter.impl.task;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class LaunchGameTask extends BukkitRunnable {

    private int timer;

    public LaunchGameTask() {
        timer = 10;
        this.runTaskTimer(UHCBase.getInstance(), 20, 20);
    }

    @Override
    public void run() {
        if(!UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
            cancel();
            return;
        }

        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby()) {
            if(timer == 0) {
                uhcPlayer.sendTitle("Partie Lancée !", ChatColor.GREEN);
                uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_BASS, 6.0F, 1.0F);
            }
            else {
                uhcPlayer.sendTitle("Début dans " + timer, ChatColor.GREEN);
                uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_PLING, 6.0F, 1.0F);
            }
        }

        timer--;

        if(timer < 0) {
            cancel();
            Bukkit.getScheduler().runTaskLater(UHCBase.getInstance(), TeleportingTask::new, 20);
        }
    }
}
