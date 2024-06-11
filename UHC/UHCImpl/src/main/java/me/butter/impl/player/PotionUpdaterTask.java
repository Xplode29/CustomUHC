package me.butter.impl.player;

import me.butter.api.UHCAPI;
import me.butter.api.player.PlayerState;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PotionUpdaterTask extends BukkitRunnable {

    public PotionUpdaterTask() {
        this.runTaskTimer(UHCImpl.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        for (UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
            if (uhcPlayer.getPlayer() == null || uhcPlayer.getPlayerState() == PlayerState.DEAD || uhcPlayer.getPotionEffects() == null) {
                continue;
            }

            List<Potion> toRemove = new ArrayList<>();

            for (Potion potion : uhcPlayer.getPotionEffects()) {
                if (!potion.isValid() || !potion.isActive()) {
                    continue;
                }

                if(potion.getDuration() == -1) {
                    uhcPlayer.getPlayer().removePotionEffect(potion.getEffect());
                    uhcPlayer.getPlayer().addPotionEffect(new PotionEffect(potion.getEffect(), Integer.MAX_VALUE, potion.getLevel() - 1, false, false));
                }
                else {
                    if(!uhcPlayer.getPlayer().hasPotionEffect(potion.getEffect()) && potion.getDuration() > 0) {
                        uhcPlayer.getPlayer().addPotionEffect(new PotionEffect(potion.getEffect(), potion.getDuration() * 20, potion.getLevel() - 1, false, false));
                    }
                    else {
                        if(potion.getDuration() <= 0) toRemove.add(potion);
                        else potion.setDuration(potion.getDuration() - 1);
                    }
                }
            }

            for(Potion potion : toRemove) {
                uhcPlayer.removePotionEffect(potion.getEffect());
            }
        }
    }
}
