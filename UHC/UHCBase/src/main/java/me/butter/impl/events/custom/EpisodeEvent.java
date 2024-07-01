package me.butter.impl.events.custom;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EpisodeEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private int episode;

    public EpisodeEvent(int episode) {
        this.episode = episode;
    }

    public int getEpisode() {
        return episode;
    }
}
