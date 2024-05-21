package me.butter.impl.events;

import me.butter.api.UHCAPI;
import me.butter.api.module.Module;
import me.butter.impl.UHCImpl;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.HandlerList;

public class EventUtils {

    public static void callEvent(Event event) {
        Module module = UHCAPI.getInstance().getModuleHandler().getModule();

        if (module != null && module.getPlugin() != null) {
            HandlerList.getRegisteredListeners(module.getPlugin()).forEach(registeredListener -> {
                try {
                    registeredListener.callEvent(event);
                } catch (EventException e) {
                    e.printStackTrace();
                }
            });
        }

        HandlerList.getRegisteredListeners(UHCImpl.getInstance()).forEach(registeredListener -> {
            try {
                registeredListener.callEvent(event);
            } catch (EventException e) {
                e.printStackTrace();
            }
        });
    }
}