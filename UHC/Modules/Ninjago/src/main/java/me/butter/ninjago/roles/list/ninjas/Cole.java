package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.items.SpinjitzuPower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;

public class Cole extends NinjagoRole {

    RockPower rockPower;

    public Cole() {
        super("Cole", "/roles/ninjas/cole", Arrays.asList(
                new RockPower(),
                new SpinjitzuPower(ChatColor.GRAY)
        ));
        for(Power power : getPowers()) {
            if(power instanceof RockPower) {
                rockPower = (RockPower) power;
                break;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Vous possédez Force 1 (20%) permanent"};
    }

    @Override
    public boolean isNinja() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addStrength(20);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damaged = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
        if(damaged.equals(getUHCPlayer())) {
            if(rockPower.rockActive && rockPower.coups > 0) {
                rockPower.coups--;
            } else if (rockPower.rockActive) {
                getUHCPlayer().removeResi(20);
                getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous avez recu trop de coups"));
                rockPower.rockActive = false;
            }
        }
    }

    @EventHandler
    public void onPlayerKill(UHCPlayerDeathEvent event) {
        if(event.getKiller().equals(getUHCPlayer())) {
            rockPower.coups += 15;
            if(rockPower.coups > 60) {
                rockPower.coups = 60;
            }
        }
    }

    private static class RockPower extends ItemPower {
        boolean rockActive = false;
        int coups = 25;

        public RockPower() {
            super(ChatColor.GRAY + "Rock", Material.NETHER_STAR, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {"Lorsque cet item est activé, vous obtenez 20% de résistance. ",
                    "Cet item se désactive après avoir recu 25 coups. ",
                    "Lorsque vous faites un kill, vous obtenez 15 coups supplémentaires, avec un maximum de 60 coups. ",
                    "Vous pouvez vérifier les coups qu'il vous reste avec un clic gauche."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(clickAction == Action.RIGHT_CLICK_AIR || clickAction == Action.RIGHT_CLICK_BLOCK) {
                if(coups <= 0) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez infligé trop de coups"));
                    return false;
                }
                rockActive = !rockActive;
                if(rockActive) {
                    player.addResi(20);
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif activé"));
                }
                else {
                    player.removeResi(20);
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif désactivé"));
                }
            }
            else {
                player.sendMessage(ChatUtils.ERROR.getMessage("Il vous reste: " + coups + " coups"));
            }
            return false;
        }
    }
}
