package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Jay extends NinjagoRole {

    private static boolean foudreActive = false;

    public Jay() {
        super("Jay", "doc", Arrays.asList(
            new LightningCommand(),
            new SpinjitzuPower()
        ));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez 30% de speed permanent"
        };
    }

    @Override
    public boolean isNinja() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(30);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        Player targetPlayer = (Player) event.getEntity();

        if(damager.equals(getUHCPlayer())) {
            if(foudreActive) {
                if((new Random()).nextInt(100) <= 15) {
                    targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.EXPLODE, 3.0f, 1.0f);
                    targetPlayer.getWorld().strikeLightningEffect(targetPlayer.getLocation());

                    targetPlayer.setHealth(Math.max(0, targetPlayer.getHealth() - 3.0));
                }
            }
        }
    }

    private static class LightningCommand extends CommandPower {

        public LightningCommand() {
            super("Foudre", "foudre", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Active / désactive votre passif. Lorsqu'il est activé, vous avez 15% de chances de faire tomber un éclair qui inflige 1,5 coeurs de dégâts sur la personne frappé"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            foudreActive = !foudreActive;
            if(foudreActive) {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif activé"));
            }
            else {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif désactivé"));
            }
            return false;
        }
    }

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.BLUE + "Spinjitzu", Material.NETHER_STAR, 5 * 60, -1);
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

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.BLUE);

            return true;
        }
    }
}
