package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.NinjagoV2;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.powers.SpinjitzuPower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Kai extends NinjagoRole {

    boolean isFire = false;

    public Kai() {
        super("Kai", "/roles/ninjas/kai", new FireSpinjitzu());
        addPower(new FireCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez §cForce 1§r permanent",
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addStrength(15);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        if(getUHCPlayer() == damager && isFire) {
            event.getEntity().setFireTicks(20 * 3);
        }
    }

    private class FireCommand extends CommandPower {

        public FireCommand() {
            super("Passif", "fire", 0, -1);
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Lorsque ce passif est activé, Kai obtient Résistance au feu permanent et enflamme les joueurs frappés, mais perd Force 1."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            isFire = !isFire;
            if(isFire) {
                getUHCPlayer().removeStrength(15);
                getUHCPlayer().addPotionEffect(PotionEffectType.FIRE_RESISTANCE, -1, 1);

                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif activé"));
            }
            else {
                getUHCPlayer().addStrength(15);
                getUHCPlayer().removePotionEffect(PotionEffectType.FIRE_RESISTANCE);

                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif désactivé"));
            }

            return false;
        }
    }

    private static class FireSpinjitzu extends SpinjitzuPower {

        public FireSpinjitzu() {
            super(ChatColor.RED, Material.NETHER_STAR, 15 * 60, -1, new String[] {
                    "Enflamme tous les joueurs dans le rayon de l'attaque. Ces joueurs ne peuvent pas s'éteindre pendant 15 secondes."
            });
        }

        @Override
        public void applyEffects(UHCPlayer player, List<UHCPlayer> players) {
            new FireRunnable(players).runTaskTimer(NinjagoV2.getInstance(), 0, 5);
        }

        private static class FireRunnable extends BukkitRunnable {

            private final List<UHCPlayer> players;
            private int timer;

            public FireRunnable(List<UHCPlayer> players) {
                this.players = players;

                this.timer = 15 * 4;
            }

            @Override
            public void run() {
                if (timer <= 0) {
                    cancel();
                }

                for (UHCPlayer player : players) {
                    player.getPlayer().setFireTicks(3 * 20);
                }

                timer--;
            }
        }
    }
}
