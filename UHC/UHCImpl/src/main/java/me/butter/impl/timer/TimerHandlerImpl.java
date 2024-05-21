package me.butter.impl.timer;

import me.butter.api.UHCAPI;
import me.butter.api.module.Module;
import me.butter.api.timer.Timer;
import me.butter.api.timer.TimerHandler;
import me.butter.impl.timer.list.*;

import java.util.ArrayList;
import java.util.List;

public class TimerHandlerImpl implements TimerHandler {

    private List<Timer> timers;

    public TimerHandlerImpl() {
        timers = new ArrayList<>();

        this.addTimer(new InvincibilityTimer());
        this.addTimer(new PVPTimer());
        this.addTimer(new MeetupTimer());
        this.addTimer(new BorderTimer());
    }

    @Override
    public List<Timer> getTimers() {
        return timers;
    }

    @Override
    public Timer getTimer(Class<? extends Timer> timerClass) {
        for(Timer timer : timers) {
            if(timer.getClass() == timerClass) {
                return timer;
            }
        }
        return null;
    }

    @Override
    public void addTimer(Timer timer) {
        if(!timers.contains(timer)) {
            timers.add(timer);
        }
    }

    @Override
    public void removeTimer(Class<? extends Timer> timerClass) {
        timers.removeIf(timer -> timer.getClass() == timerClass);
    }
}