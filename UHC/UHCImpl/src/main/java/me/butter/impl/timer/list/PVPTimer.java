package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

public class PVPTimer extends AbstractTimer {

    public PVPTimer() {
        super("PVP", Material.DIAMOND_SWORD, 20);
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Définit le temps d'activation du PVP"};
    }

    @Override
    public void onTimerDone() {
        Bukkit.broadcastMessage(ChatUtils.WARNING.getMessage("Le PVP est maintenant actif."));
        UHCAPI.getInstance().getGameHandler().getGameConfig().setPvp(true);
        UHCAPI.getInstance().getGameHandler().getGameConfig().setChatEnabled(false);
    }

    @Override
    public void onUpdate(int timer) {
        if ((getMaxTimer() - 300) - timer == 0) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Le PVP sera actif dans 5 minutes !"));
        }
    }
}
