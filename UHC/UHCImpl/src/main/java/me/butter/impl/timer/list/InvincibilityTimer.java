package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.ChatUtils;
import me.butter.api.utils.GraphicUtils;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class InvincibilityTimer extends AbstractTimer {

    public InvincibilityTimer() {
        super("Invinciblité", Material.IRON_CHESTPLATE, 60);
    }

    @Override
    public void onTimerDone() {
        UHCAPI.getInstance().getGameHandler().getGameConfig().setInvincibility(false);

        Bukkit.broadcastMessage(ChatUtils.WARNING.getMessage("Vous n'êtes plus invincible."));
    }

    @Override
    public void onUpdate(int timer) {
        if (getMaxTimer() >= timer) {
            UHCAPI.getInstance().getPlayerHandler().getPlayers().forEach(uhcPlayer -> uhcPlayer.sendActionBar(
                    "§eInvincibilité §8" + " §8[" + GraphicUtils.getProgressBar(timer, getMaxTimer(), 10, '|', ChatColor.GREEN, ChatColor.GRAY) + "§8]"
            ));
        }
    }
}
