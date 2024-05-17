package me.butter.impl.timer.list;


import me.butter.api.UHCAPI;
import me.butter.api.utils.ChatUtils;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldBorder;

public class BorderTimer extends AbstractTimer {
    public BorderTimer() {
        super(80 * 60);
    }

    @Override
    public String getName() {
        return "Bordure";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Définit le temps de réduction de la bordure"};
    }

    @Override
    public void onTimerDone() {
        UHCAPI.get().getGameHandler().getWorldConfig().setBorderMoving(true);
        Bukkit.broadcastMessage(ChatUtils.WARNING.getMessage("La bordure commence à se réduire."));
        moveBorder();
    }


    @Override
    public void onUpdate(int timer) {
        if ((getMaxTimer() - 300) - timer == 0) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("La bordure vas se réduire dans 5 minutes !"));
        }
    }

    @Override
    public Material getIcon() {
        return Material.BARRIER;
    }

    public void moveBorder() {
        WorldBorder worldBorder = UHCAPI.get().getWorldHandler().getWorld().getWorldBorder();
        int finalSize = UHCAPI.get().getGameHandler().getWorldConfig().getFinalBorderSize();
        int blocksSecond = 1;
        double size = worldBorder.getSize();
        double dif = Math.abs(size - finalSize);
        double time = dif / blocksSecond;
        worldBorder.setSize(finalSize, (long) time);
    }
}
