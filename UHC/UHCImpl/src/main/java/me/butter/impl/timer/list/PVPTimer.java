package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.ChatUtils;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class PVPTimer extends AbstractTimer {
    public PVPTimer() {
        super(20);
    }

    @Override
    public String getName() {
        return "PVP";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Définit le temps d'activation du PVP"};
    }

    @Override
    public void onTimerDone() {
        Bukkit.broadcastMessage(ChatUtils.WARNING.getMessage("Le PVP est maintenant actif."));
        UHCAPI.get().getGameHandler().getGameConfig().setPvp(true);
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public void onUpdate(int timer) {
        if ((getMaxTimer() - 300) - timer == 0) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Le PVP sera actif dans 5 minutes !"));
        }
        if (!UHCAPI.get().getGameHandler().getGameConfig().isInvincibility()) {
            if (!UHCAPI.get().getGameHandler().getGameConfig().isPvp()) {
                UHCAPI.get().getPlayerHandler().getPlayers().forEach(uhcPlayer -> uhcPlayer.sendActionBar(
                        "§cPVP §8[" + getProgressBar(timer, getMaxTimer(), 10, '|', ChatColor.GREEN, ChatColor.GRAY) + "§8]"
                ));
            }
        }
    }
    
    public String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return com.google.common.base.Strings.repeat("" + completedColor + symbol, progressBars)
                + com.google.common.base.Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
}
