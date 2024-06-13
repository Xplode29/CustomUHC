package me.butter.api.timer;

import org.bukkit.Material;

public interface Timer {

    String getName();

    String[] getDescription();

    Material getIcon();

    int getMaxTimer();

    void setMaxTimer(int timer);

    boolean isFired(); void fireTimer();

    void onTimerDone();

    void onUpdate(int timer);
}
