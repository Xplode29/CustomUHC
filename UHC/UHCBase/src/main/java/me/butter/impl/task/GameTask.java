package me.butter.impl.task;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.module.Module;
import me.butter.api.module.roles.Role;
import me.butter.api.scenario.Scenario;
import me.butter.api.timer.Timer;
import me.butter.impl.UHCBase;
import me.butter.impl.events.EventUtils;
import me.butter.impl.events.custom.DayNightChangeEvent;
import me.butter.impl.events.custom.EpisodeEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTask extends BukkitRunnable {
    public GameTask() {
        for (Scenario scenario : UHCAPI.getInstance().getScenarioHandler().getEnabledScenarios()) {
            scenario.onStartGame();
        }

        this.runTaskTimer(UHCBase.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.ENDING) {
            if(UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().isEmpty()) {
                UHCAPI.getInstance().reset();
            }
        }

        UHCAPI.getInstance().getGameHandler().getGameConfig().setTimer(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer() + 1);

        for (Scenario scenario : UHCAPI.getInstance().getScenarioHandler().getEnabledScenarios()) {
            scenario.onUpdate(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer());
        }

        for (Timer timer : UHCAPI.getInstance().getTimerHandler().getTimers()) {
            if(!timer.isFired() && timer.getMaxTimer() > UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) {
                timer.onUpdate(UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer());
            }

            if(!timer.isFired() && timer.getMaxTimer() == UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) {
                timer.setFired(timer.onTimerDone());
            }
        }

        int episode = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer() /
                UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration();
        if(episode != UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisode()) {
            UHCAPI.getInstance().getGameHandler().getGameConfig().setEpisode(episode);

            EventUtils.callEvent(new EpisodeEvent(episode));
        }

        int timerSinceLastPeriod = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer() %
                UHCAPI.getInstance().getGameHandler().getGameConfig().getDayDuration();

        if (timerSinceLastPeriod == 0 && UHCAPI.getInstance().getGameHandler().getGameConfig().isDayCycleActivated()) {
            boolean isNewDay = !UHCAPI.getInstance().getGameHandler().getGameConfig().isDay();
            UHCAPI.getInstance().getGameHandler().getGameConfig().setDay(isNewDay);

            if(UHCAPI.getInstance().getModuleHandler().hasModule()) {
                Module module = UHCAPI.getInstance().getModuleHandler().getModule();
                if(module.hasRoles()) {
                    for(Role role : module.getRolesList()) {
                        if (isNewDay) {
                            role.onDay();
                        } else {
                            role.onNight();
                        }
                    }
                }
            }

            EventUtils.callEvent(new DayNightChangeEvent(isNewDay));
        }
    }
}