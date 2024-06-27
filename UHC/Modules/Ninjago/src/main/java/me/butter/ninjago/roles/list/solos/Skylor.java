package me.butter.ninjago.roles.list.solos;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.goldenNinja.ChatEffectChooser;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Skylor extends NinjagoRole {

    public Skylor() {
        super("Skylor", "/roles/solitaires/skylor", new CopyCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "A chaque début d'épisode, vous obtenez les effets et le pseudo d'un joueur aléatoire dans un rayon de 100 blocks. "
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        List<UHCPlayer> uhcPlayers = new ArrayList<>();

        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(uhcPlayer != null && getUHCPlayer().isNextTo(uhcPlayer, 100) && uhcPlayer.getRole() != null) {
                uhcPlayers.add(uhcPlayer);
            }
        }

        if(uhcPlayers.isEmpty()) {
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Il n'y a personne autour de vous"));
            return;
        }

        Collections.shuffle(uhcPlayers);

        UHCPlayer uhcPlayer = uhcPlayers.get(0);
        getUHCPlayer().sendMessage(ChatUtils.LIST_HEADER.getMessage("Voici les effets de " + uhcPlayer.getName()));
        for(Potion potion : uhcPlayer.getPotionEffects()) {
            getUHCPlayer().sendMessage(ChatUtils.LIST_ELEMENT.getMessage(potion.getEffect().getName()));
        }
    }

    public static class CopyCommand extends TargetCommandPower {
        public CopyCommand() {
            super("Copie", "copie", 10 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Copie les effets du joueur ciblé pendant 10 minutes."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            for(Potion potion : target.getPotionEffects()) {
                player.addPotionEffect(potion.getEffect(), potion.getDuration(), potion.getLevel());
            }
            player.addResi(target.getResi());
            player.addStrength(target.getStrength());
            player.addSpeed(target.getSpeed());

            player.addMaxHealth(target.getMaxHealth() - 20);
            player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Vous avez copie les effets de " + target.getName()));

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.clearEffects();
                    player.removeMaxHealth(player.getMaxHealth() - 20);
                }
            }.runTaskLater(Ninjago.getInstance(), 10 * 60 * 20);

            return true;
        }
    }
}
