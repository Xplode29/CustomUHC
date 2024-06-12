package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.items.SpinjitzuPower;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.Random;

public class Jay extends NinjagoRole {

    private LightningCommand power;

    public Jay() {
        super("Jay", "/roles/ninjas/jay", Arrays.asList(
            new LightningCommand(),
            new SpinjitzuPower(ChatColor.BLUE)
        ));
        for(Power power : getPowers()) {
            if(power instanceof LightningCommand) {
                this.power = (LightningCommand) power;
                break;
            }
        }
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
        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
        if(damager == null || target == null || target.getPlayer() == null) return;

        if(damager.equals(getUHCPlayer())) {
            if(power.foudreActive) {
                if((new Random()).nextInt(100) <= 15) {
                    target.getPlayer().getWorld().strikeLightningEffect(target.getPlayer().getLocation());
                    target.getPlayer().getWorld().playSound(target.getPlayer().getLocation(), Sound.EXPLODE, 1.0f, 1.0f);

                    target.getPlayer().setHealth(Math.max(0.5, target.getPlayer().getHealth() - 3.0));
                }
            }
        }
    }

    private static class LightningCommand extends CommandPower {

        boolean foudreActive = false;

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
}
