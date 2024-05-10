package me.butter.impl.player;

import me.butter.api.UHCAPI;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class PotionUpdaterTask extends BukkitRunnable {

    public PotionUpdaterTask() {
        this.runTaskTimer(UHCImpl.get(), 0, 20);
    }

    @Override
    public void run() {
        for (UHCPlayer uhcPlayer : UHCAPI.get().getPlayerHandler().getPlayersInGame()) {
            if (uhcPlayer.getPlayer() == null) {
                continue;
            }

            for (Potion potion : uhcPlayer.getPotionEffects()) {
                if (!potion.isValid() || !potion.isActive()) {
                    continue;
                }

                uhcPlayer.getPlayer().removePotionEffect(potion.getEffect());

                if(potion.getDuration() == -1) {
                    uhcPlayer.getPlayer().addPotionEffect(new PotionEffect(potion.getEffect(), 20 * 2, potion.getLevel() + 1, false, false));
                }

                else {
                    potion.setDuration(potion.getDuration() - 1);
                    uhcPlayer.getPlayer().addPotionEffect(new PotionEffect(potion.getEffect(), potion.getDuration(), potion.getLevel() + 1, false, false));
                }
            }
        }
    }
}
