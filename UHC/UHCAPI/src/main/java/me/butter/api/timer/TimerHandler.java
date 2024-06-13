package me.butter.api.timer;

import java.util.List;

public interface TimerHandler {

    List<Timer> getTimers();

    Timer getTimer(Class<? extends Timer> timerClass);
    Timer getTimer(String timerName);

    void addTimer(Timer timer);

    void removeTimer(Class<? extends Timer> timerClass);
}