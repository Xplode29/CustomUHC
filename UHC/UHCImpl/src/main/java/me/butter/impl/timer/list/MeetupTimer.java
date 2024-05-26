package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class MeetupTimer extends AbstractTimer {

    public MeetupTimer() {
        super("Meetup", Material.GOLD_PICKAXE, 60 * 60);
    }

    @Override
    public void onTimerDone() {
        UHCAPI.getInstance().getGameHandler().getGameConfig().setMeetup(true);

        Bukkit.broadcastMessage(ChatUtils.WARNING.getMessage("Le Meetup est maintenant actif."));
    }

    @Override
    public void onUpdate(int timer) {
        if ((getMaxTimer() - 300) - timer == 0) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Le Meetup sera actif dans 5 minutes !"));
        }
    }
}
