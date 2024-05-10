package me.butter.impl.events.custom;

import me.butter.api.player.UHCPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PlayerDeathEvent;

public class UHCPlayerDeathEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }


    private UHCPlayer killer;
    private UHCPlayer victim;
    private PlayerDeathEvent originalEvent;
    private boolean cancelled;

    public UHCPlayerDeathEvent(PlayerDeathEvent originalEvent, UHCPlayer killer, UHCPlayer victim) {
        this.originalEvent = originalEvent;
        this.killer = killer;
        this.victim = victim;

        this.cancelled = false;
    }

    public UHCPlayer getKiller() {
        return killer;
    }

    public UHCPlayer getVictim() {
        return victim;
    }

    public PlayerDeathEvent getOriginalEvent() {
        return originalEvent;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
