package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.ChatUtils;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class InvincibilityTimer extends AbstractTimer {
    public InvincibilityTimer() {
        super(60);
    }

    @Override
    public String getName() {
        return "Invinciblité";
    }

    @Override
    public Material getIcon() {
        return Material.IRON_CHESTPLATE;
    }

    @Override
    public void onTimerDone() {
        UHCAPI.get().getGameHandler().getGameConfig().setInvincibility(false);

        Bukkit.broadcastMessage(ChatUtils.WARNING.getMessage("Vous n'êtes plus invincible."));
    }

    @Override
    public void onUpdate(int timer) {
        if (getMaxTimer() >= timer) {
            UHCAPI.get().getPlayerHandler().getPlayers().forEach(uhcPlayer -> uhcPlayer.sendActionBar(
                    "§eInvincibilité §8" + " §8[" + getProgressBar(timer, getMaxTimer(), 10, '|', ChatColor.GREEN, ChatColor.GRAY) + "§8]"
            ));
        }
    }

    public String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return com.google.common.base.Strings.repeat("" + completedColor + symbol, progressBars)
                + com.google.common.base.Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }
}
