package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.UHCBase;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.powers.SpinjitzuPower;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Jay extends NinjagoRole {

    private boolean foudreActive = false;
    private int coups = 0;

    public Jay() {
        super("Jay", "/roles/ninjas/jay", new LightningSpinjitzu());
        addPower(new LightningCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez §9Speed 1§r permanent"
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(20);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
        if(damager == null || target == null || target.getPlayer() == null) return;

        if(damager.equals(getUHCPlayer())) {
            if(foudreActive) {
                coups++;
                getUHCPlayer().sendActionBar("Coups: " + coups + "/10");

                if(coups >= 10) {
                    coups = 0;
                    target.getPlayer().getWorld().strikeLightningEffect(target.getPlayer().getLocation());
                    target.getPlayer().getWorld().playSound(target.getPlayer().getLocation(), Sound.EXPLODE, 1.0f, 1.0f);

                    target.getPlayer().setHealth(Math.max(0.5, target.getPlayer().getHealth() - 2.0));
                }
            }
        }
    }

    private class LightningCommand extends CommandPower {

        public LightningCommand() {
            super("§9Passif", "foudre", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Lorsqu'il est activé, vous faites tomber un éclair qui inflige §c1 coeur§r de dégâts sur la personne frappé §ltous les 10 coups§r. "};
        }

        @Override
        public boolean hideCooldowns() {
            return true;
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

    private static class LightningSpinjitzu extends SpinjitzuPower {

        public LightningSpinjitzu() {
            super(ChatColor.BLUE, Material.NETHER_STAR, 15 * 60, -1, new String[] {
                    "Les joueurs touchés sont immobilisés pendant 2 secondes. Ils sont invulnérables pendant cette période de temps."
            });
        }

        @Override
        public void applyEffects(UHCPlayer player, List<UHCPlayer> players) {
            Bukkit.getScheduler().runTaskLater(UHCBase.getInstance(), () -> players.forEach(p -> p.setAbleToMove(false)), 2 * 20);
            Bukkit.getScheduler().runTaskLater(UHCBase.getInstance(), () -> players.forEach(p -> p.setAbleToMove(true)), 4 * 20);
        }
    }
}
