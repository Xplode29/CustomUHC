package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.api.potion.CustomPotionEffect;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class PIXAL extends NinjagoRole {

    private boolean canUseCommand = true;

    private UHCPlayer zane;

    public PIXAL() {
        super("PIXAL", "/roles/ninjas/pixal");
        addPower(new EffectsCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "A l'annonce des roles, vous obtenez le pseudo de §aZane§r."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(zane == null ? "Il n'y a pas de zane dans cette partie" : "Zane: " + zane.getName());
    }

    @Override
    public void onDistributionFinished() {
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Zane && role.getUHCPlayer() != null) {
                zane = role.getUHCPlayer();
            }
        }
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        canUseCommand = true;
    }

    private class EffectsCommand extends TargetCommandPower {

        public EffectsCommand() {
            super("Analyse", "analyse", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Vous obtenez les effets du joueur ciblé. (1x/Episode)"
            };
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            if(!canUseCommand) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous devez attendre l'episode suivant"));
                return false;
            }

            player.sendMessage(ChatUtils.LIST_HEADER.getMessage("Voici les effets de " + target.getName()));
            for(Potion potion : target.getPotionEffects()) {
                CustomPotionEffect effect = UHCAPI.getInstance().getPotionEffectHandler().getEffect(potion.getEffect());
                player.sendMessage(ChatUtils.LIST_ELEMENT.getMessage(
                        getName(potion, effect) + " " + potion.getLevel()
                ));
            }

            canUseCommand = false;
            return true;
        }

        private String getName(Potion potion, CustomPotionEffect effect) {
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
    }
}
