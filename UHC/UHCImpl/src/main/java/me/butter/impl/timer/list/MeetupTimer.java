package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class MeetupTimer extends AbstractTimer {
    public MeetupTimer() {
        super(60);
    }

    @Override
    public String getName() {
        return "Meetup";
    }

    @Override
    public void onTimerDone() {
        UHCAPI.get().getGameHandler().getGameConfig().setMeetup(true);

        Bukkit.broadcastMessage("§rLe §cMeetup§r est maintenant activé.");
    }

    @Override
    public Material getMaterial() {
        return Material.GOLD_PICKAXE;
    }

    @Override
    public void onUpdate(int timer) {
        if ((getMaxTimer() - 300) - timer == 0) {
            Bukkit.broadcastMessage("§rLe §cMeetup§r sera actif dans 5 minutes !");
        }
    }
}
