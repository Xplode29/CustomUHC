package me.butter.impl.task;

import me.butter.api.UHCAPI;
import me.butter.api.timer.Timer;
import me.butter.impl.UHCImpl;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {

    public GameTask() {
        this.runTaskTimer(UHCImpl.get(), 0, 20);
    }

    @Override
    public void run() {
        UHCAPI.get().getGameHandler().getGameConfig().setTimer(UHCAPI.get().getGameHandler().getGameConfig().getTimer() + 1);

        for (Timer timer : UHCAPI.get().getTimerHandler().getTimers()) {
            timer.onUpdate(UHCAPI.get().getGameHandler().getGameConfig().getTimer());
            if (timer.getMaxTimer() == UHCAPI.get().getGameHandler().getGameConfig().getTimer()) {
                timer.onTimerDone();
            }
        }
    }
}