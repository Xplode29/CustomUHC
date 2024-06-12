package me.butter.ninjago.roles.list.serpents;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Acidicus extends NinjagoRole {

    private VeninCommand power;

    UHCPlayer pythor;

    public Acidicus() {
        super("Acidicus", "/roles/serpent/acidicus", Collections.singletonList(new VeninCommand()));
        for(Power power : getPowers()) {
            if(power instanceof VeninCommand) {
                this.power = (VeninCommand) power;
                break;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous n'êtes pas affecté par les effets négatifs (poison, faiblesse, lenteur, mining fatigue). ",
                "Vous conaissez l'identité de Pythor"
        };
    }

    @Override
    public void onGiveRole() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(getUHCPlayer().hasPotionEffect(PotionEffectType.POISON))
                    getUHCPlayer().removePotionEffect(PotionEffectType.POISON);
                if(getUHCPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING))
                    getUHCPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
                if(getUHCPlayer().hasPotionEffect(PotionEffectType.WEAKNESS))
                    getUHCPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
                if(getUHCPlayer().getSpeed() < 0) {
                    getUHCPlayer().addSpeed(-getUHCPlayer().getSpeed());
                }
            }
        }.runTaskTimer(Ninjago.getInstance(), 0, 20);
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Pythor && role.getUHCPlayer() != null) {
                pythor = role.getUHCPlayer();
                break;
            }
        }
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de Pythor" : "Pythor:" + pythor.getName()));
    }

    @Override
    public boolean isInList() {
        return true;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        UHCPlayer damaged = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());

        if(damager.equals(getUHCPlayer()) && damaged != null) {
            if(power.veninActive) {
                if((new Random()).nextInt(100) <= 25) {
                    damaged.addPotionEffect(PotionEffectType.POISON, 10, 1);
                }
            }
        }
    }

    private static class VeninCommand extends CommandPower {

        boolean veninActive = false;

        public VeninCommand() {
            super("Venin", "venin", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Active / désactive votre passif. Lorsqu'il est activé, vous avez 25% d'infliger Poison 1 au joueur frappé"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            veninActive = !veninActive;
            if(veninActive) {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif activé"));
            }
            else {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif désactivé"));
            }
            return false;
        }
    }

}
