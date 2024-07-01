package me.butter.ninjago.roles.list.solos;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.api.potion.CustomPotionEffect;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

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
            CustomPotionEffect effect = UHCAPI.getInstance().getPotionEffectHandler().getEffect(potion.getEffect());
            getUHCPlayer().sendMessage(ChatUtils.LIST_ELEMENT.getMessage(
                    getName(potion, effect) + " " + potion.getLevel()
            ));
        }
    }

    private static String getName(Potion potion, CustomPotionEffect effect) {
        String name = effect == null ? potion.getEffect().getName() : effect.getName();

        if(effect == null) {
            if(potion.getEffect() == PotionEffectType.DAMAGE_RESISTANCE) name = "Resistance";
            if(potion.getEffect() == PotionEffectType.ABSORPTION) name = "Absorption";
            if(potion.getEffect() == PotionEffectType.BLINDNESS) name = "Cecite";
            if(potion.getEffect() == PotionEffectType.CONFUSION) name = "Nausee";
            if(potion.getEffect() == PotionEffectType.FAST_DIGGING) name = "Haste";
            if(potion.getEffect() == PotionEffectType.HEALTH_BOOST) name = "Vie Ameliorée";
            if(potion.getEffect() == PotionEffectType.HUNGER) name = "Faim";
            if(potion.getEffect() == PotionEffectType.NIGHT_VISION) name = "Vision nocturne";
            if(potion.getEffect() == PotionEffectType.SATURATION) name = "Saturation";
            if(potion.getEffect() == PotionEffectType.WITHER) name = "Wither";
        }
        return name;
    }

    public static class CopyCommand extends TargetCommandPower {
        public CopyCommand() {
            super("Copie", "copie", 10 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Copie les effets du joueur ciblé."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            player.clearEffects();
            player.removeMaxHealth(player.getMaxHealth() - 20);

            for(Potion potion : target.getPotionEffects()) {
                player.addPotionEffect(potion.getEffect(), potion.getDuration(), potion.getLevel());
            }
            player.addResi(target.getResi());
            player.addStrength(target.getStrength());
            player.addSpeed(target.getSpeed());

            player.addMaxHealth(target.getMaxHealth() - 20);
            player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Vous avez copié les effets de " + target.getName()));

            return true;
        }

    }
}
