package me.butter.impl.timer;

import me.butter.api.timer.Timer;
import org.bukkit.Material;

public abstract class AbstractTimer implements Timer {

    private int maxTimer;
    String name;
    Material icon;

    public AbstractTimer(String name, Material icon, int maxTimer) {
        this.name = name;
        this.icon = icon;
        this.maxTimer = maxTimer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Material getIcon() {
        return icon;
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