package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
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
    public boolean onTimerDone() {
        Bukkit.broadcastMessage(ChatUtils.ERROR.getMessage("Le PVP est maintenant actif."));
        UHCAPI.getInstance().getGameHandler().getGameConfig().setPvp(true);

        return true;
    }

    @Override
    public void onUpdate(int timer) {
        if ((getMaxTimer() - 5 * 60) - timer == 0) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Le PVP sera actif dans 5 minutes !"));
        }

        if ((getMaxTimer() - 60) - timer == 0) {
            for(UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getAllPlayers()) {
                if(player.getPlayer() == null) continue;
                player.getPlayer().setHealth(player.getPlayer().getMaxHealth());
            }
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Final heal !"));
        }
    }
}
