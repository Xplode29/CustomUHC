package me.butter.impl.events;

import me.butter.impl.UHCImpl;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;

public class EventUtils {

    public static void callEventInModule(Event event) {
        HandlerList.getRegisteredListeners(UHCImpl.get()).forEach(registeredListener -> {
            try {
                registeredListener.callEvent(event);
            } catch (EventException e) {
                e.printStackTrace();
            }
        });
    }
}