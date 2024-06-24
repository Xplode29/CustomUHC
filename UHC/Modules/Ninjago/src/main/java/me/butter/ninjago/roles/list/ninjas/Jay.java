package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleUtils;
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

    private LightningCommand power;

    private int coups = 0;

    public Jay() {
        super("Jay", "/roles/ninjas/jay", new SpinjitzuPower());
        addPower(new LightningCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Speed 1 permanent"
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
            if(power.foudreActive) {
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

    private static class LightningCommand extends CommandPower {

        boolean foudreActive = false;

        public LightningCommand() {
            super("Foudre", "foudre", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Active / désactive votre passif. " +
                    "Lorsqu'il est activé, vous faites tomber un éclair qui inflige 1 coeurs de dégâts sur la personne frappé tous les 10 coups. "};
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
            super(ChatColor.BLUE + "Spinjitzu", Material.NETHER_STAR, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "À l'activation, repousse tous les joueurs dans un rayon de 10 blocks.",
                    "vous obtenez 10% de speed pendant 2 minutes."
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

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.BLUE);
            return true;
        }
    }
}
