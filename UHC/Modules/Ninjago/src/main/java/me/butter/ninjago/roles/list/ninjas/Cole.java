package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class Cole extends NinjagoRole {
    private static boolean rockActive = false;
    private static int coups = 25;

    public Cole() {
        super("Cole", "soon", Arrays.asList(
                new RockPower(),
                new SpinjitzuPower()
        ));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 (20%) permanent"
        };
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

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        if(damager.equals(getUHCPlayer())) {
            if(rockActive && coups > 0) {
                coups--;
            } else if (rockActive) {
                getUHCPlayer().removeResi(20);
                getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous avez infligé trop de coups"));
                rockActive = false;
            }
        }
    }

    @EventHandler
    public void onPlayerKill(UHCPlayerDeathEvent event) {
        if(event.getKiller().equals(getUHCPlayer())) {
            coups += 15;
            if(coups > 60) {
                coups = 60;
            }
        }
    }

    private static class RockPower extends ItemPower {
        public RockPower() {
            super(ChatColor.GRAY + "Rock", Material.NETHER_STAR, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {"Lorsque cet item est activé, vous obtenez 20% de résistance. ",
                    "Cet item se désactive après avoir infligé 25 coups. ",
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

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.BLACK + "Spinjitzu", Material.NETHER_STAR, 5 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {"À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            List<Entity> nearbyEntities = player.getPlayer().getNearbyEntities(4, 2, 4);
            Location center = player.getPlayer().getLocation();
            for(Entity entity : nearbyEntities) {
                double angle = Math.atan2(entity.getLocation().getZ() - center.getZ(), entity.getLocation().getX() - center.getX());
                Vector newVelocity = new Vector(
                        1.5 * Math.cos(angle),
                        0.5, //* Math.signum(entity.getLocation().getY() - center.getY()),
                        1.5 * Math.sin(angle)
                );
                entity.setVelocity(newVelocity);
            }

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.fromRGB(28, 28, 28));

            return true;
        }
    }
}
