package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PIXAL extends NinjagoRole {
    public PIXAL() {
        super("PIXAL", "doc", Collections.singletonList(new EffectsCommand()));
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "A l'annonce des roles, vous obtenez le pseudo de Zane."
        };
    }

    @Override
    public void onDistributionFinished() {
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Zane && role.getUHCPlayer() != null) {
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Zane:" + role.getUHCPlayer().getName()));
                return;
            }
        }
        getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Il n'y a pas de zane dans cette partie"));
    }

    private static class EffectsCommand extends TargetCommandPower {

        public EffectsCommand() {
            super("Analyse", "analyse", UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration(), -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"A l'execution, vous obtenez les effets du joueur ciblé"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Voici les effets de " + target.getName()));
            player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage("Coeurs: " + target.getPlayer().getMaxHealth()));

            for(Potion potion : target.getPotionEffects()) {
                player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage(potion.getEffect().getName()));
            }
            return true;
        }
    }
}
