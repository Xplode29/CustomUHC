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
            List<Potion> toRemovePacket = new ArrayList<>();

            for (Potion potion : uhcPlayer.getPotionEffects()) {
                if (!potion.isValid() || !potion.isActive()) {
                    continue;
                }

                if(potion.isPacket()) {
                    if(potion.getDuration() != -1) {
                        if(potion.getDuration() <= 0) toRemovePacket.add(potion);
                        else potion.setDuration(potion.getDuration() - 1);
                    }
                }
                else {
                    if(potion.getDuration() == -1) {
                        if(!uhcPlayer.getPlayer().hasPotionEffect(potion.getEffect())) {
                            uhcPlayer.getPlayer().addPotionEffect(new PotionEffect(potion.getEffect(), Integer.MAX_VALUE, potion.getLevel() - 1, false, false));
                        }
                    }
                    else {
                        if(potion.getDuration() > 0) {
                            potion.setDuration(potion.getDuration() - 1);
                        }
                        else {
                            toRemove.add(potion);
                        }
                    }
                }
            }

            for(Potion potion : toRemove) {
                uhcPlayer.removePotionEffect(potion.getEffect());
            }
            for(Potion potion : toRemovePacket) {
                uhcPlayer.removePacketPotionEffect(potion.getEffect());
            }
        }
    }
}
