package me.butter.api.timer;

import org.bukkit.Material;

public interface Timer {

    String getName();

    String[] getDescription();

    Material getMaterial();

    int getMaxTimer();

    void setMaxTimer(int timer);

    void onTimerDone();

    void onUpdate(int timer);
}
