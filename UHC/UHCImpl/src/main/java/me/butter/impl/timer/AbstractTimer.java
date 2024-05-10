package me.butter.impl.timer;

import me.butter.api.timer.Timer;

public abstract class AbstractTimer implements Timer {

    private int maxTimer;

    public AbstractTimer(int maxTimer) {
        this.maxTimer = maxTimer;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public int getMaxTimer() {
        return this.maxTimer;
    }

    @Override
    public void setMaxTimer(int timer) {
        this.maxTimer = timer;
    }
}