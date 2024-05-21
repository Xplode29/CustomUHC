package me.butter.impl.events.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DayNightChangeEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private boolean isDay;

    public DayNightChangeEvent(boolean isDay) {
        this.isDay = isDay;
    }

    public boolean isDay() {
        return isDay;
    }
}
