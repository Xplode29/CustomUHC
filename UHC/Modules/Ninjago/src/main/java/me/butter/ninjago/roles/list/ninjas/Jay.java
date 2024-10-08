package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleEffects;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class Jay extends NinjagoRole {

    private boolean foudreActive = false;
    private int coups = 0;

    public Jay() {
        super("Jay", "/roles/ninjas-14/jay", new SpinjitzuPower());
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
            super("§9Foudre", "foudre", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Active / désactive votre passif. Lorsqu'il est activé, vous faites tomber un éclair qui inflige §c1 coeur§r de dégâts sur la personne frappé §ltous les 10 coups§r. "};
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

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super("§9Spinjitzu", Material.NETHER_STAR, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Repousse tous les joueurs dans un rayon de 10 blocks. Vous obtenez §910% de speed§r pendant 2 minutes."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            Location center = player.getPlayer().getLocation();

            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                if(uhcPlayer == null || uhcPlayer == player || uhcPlayer.getPlayer() == null) continue;

                if(uhcPlayer.isNextTo(player, 10)) {
                    double angle = Math.atan2(uhcPlayer.getLocation().getZ() - center.getZ(), uhcPlayer.getLocation().getX() - center.getX());
                    Vector newVelocity = new Vector(
                            1.5 * Math.cos(angle),
                            0.5,
                            1.5 * Math.sin(angle)
                    );
                    uhcPlayer.getPlayer().setVelocity(newVelocity);
                }
            }

            player.addSpeed(10);
            Bukkit.getScheduler().runTaskLater(Ninjago.getInstance(), () -> player.removeSpeed(10), 2 * 60 * 20);

            ParticleEffects.tornadoEffect(player.getPlayer(), Color.BLUE);
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous utilisez votre Spinjitzu !"));
            return true;
        }
    }
}
