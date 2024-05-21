package me.butter.impl.task;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.BlockUtils;
import me.butter.impl.UHCImpl;
import me.butter.impl.player.PotionUpdaterTask;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.tab.list.GameTab;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class StartTask extends BukkitRunnable {

    private int timer;

    public StartTask() {
        this.timer = 10;
        UHCAPI.getInstance().getGameHandler().setGameState(GameState.STARTING);

        this.runTaskTimer(UHCImpl.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        this.timer -= 1;

        for (UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
            if(timer == 0) {
                uhcPlayer.clearEffects();

                uhcPlayer.sendTitle("Bonne Chance !", ChatColor.GREEN);
                uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_BASS, 6.0F, 1.0F);
            }
            else {
                uhcPlayer.sendTitle("" + timer, ChatColor.GREEN);
                uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_PLING, 6.0F, 1.0F);
            }
        }

        if (timer == 0) {
            UHCAPI.getInstance().getGameHandler().setGameState(GameState.IN_GAME);
            this.cancel();

            new GameTask();
        }
    }
}
