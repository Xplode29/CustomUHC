package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class InvincibilityTimer extends AbstractTimer {

    public InvincibilityTimer() {
        super("Invinciblité", Material.IRON_CHESTPLATE, 10);
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Definit le temps d'invincibilité des joueurs."
        };
    }

    @Override
    public boolean onTimerDone() {
        UHCAPI.getInstance().getGameHandler().getGameConfig().setInvincibility(false);
        Bukkit.broadcastMessage(ChatUtils.ERROR.getMessage("Vous n'êtes plus invincible."));

        return true;
    }

    @Override
    public void onUpdate(int timer) {
        if (getMaxTimer() >= timer) {
            UHCAPI.getInstance().getPlayerHandler().getPlayersConnected().forEach(uhcPlayer -> uhcPlayer.sendActionBar(
                    "§eInvincibilité §8" + " §8[" + GraphicUtils.getProgressBar(timer, getMaxTimer(), 10, '|', ChatColor.GREEN, ChatColor.GRAY) + "§8]"
            ));
        }
    }
}
