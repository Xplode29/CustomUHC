package me.butter.ninjago.roles.list.ninjas;

import com.google.common.collect.ImmutableMap;
import jdk.nashorn.internal.ir.annotations.Immutable;
import me.butter.api.UHCAPI;
import me.butter.api.module.power.EnchantedItemPower;
import me.butter.api.module.power.ItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class Dareth extends NinjagoRole {
    public Dareth() {
        super("Dareth", "/roles/ninjas/dareth", new Bow());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez Faiblesse 1 la nuit.",
                "A chaque debut d'episode, vous obtenez le nombre de joueurs autour de vous dans un rayon de 50 blocks maitrisant un element"
        };
    }

    @Override
    public void onGiveRole() {
        if(!UHCAPI.getInstance().getGameHandler().getGameConfig().isDay()) {
            getUHCPlayer().removeStrength(15);
        }
    }

    @Override
    public void onDay() {
        getUHCPlayer().addStrength(15);
    }

    @Override
    public void onNight() {
        getUHCPlayer().removeStrength(15);
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        int count = 0;
        for(UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(player == getUHCPlayer()) continue;

            if(getUHCPlayer().isNextTo(player, 50)) {
                if(player.getRole() instanceof NinjagoRole && ((NinjagoRole) player.getRole()).isElementalMaster()) {
                    count++;
                }
            }
        }

        if(count == 0) {
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Il n'y a pas de joueurs maitrisant un element autour de vous"));
            return;
        }

        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Il y a " + count + " joueurs maitrisant un element autour de vous"));
    }

    private static class Bow extends EnchantedItemPower {
        public Bow() {
            super("Arc", Material.BOW, ImmutableMap.of(Enchantment.ARROW_DAMAGE, 3, Enchantment.ARROW_KNOCKBACK, 1));
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Un arc Power 3 Punch 1",
            };
        }
    }
}
