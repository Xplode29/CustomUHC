package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.TargetBlockItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.NinjagoV2;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.powers.SpinjitzuPower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class Cole extends NinjagoRole {

    boolean canBeGhost = false;
    boolean ghost = false;

    int coups = 0;

    public Cole() {
        super("Cole", "/roles/ninjas/cole", new StoneWall(), new EarthSpinjitzu());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A partir du 3eme episode, vous avez 3% de chance de devenir un fantome lors de la mort d'un joueur.",
                "Lorsque vous etes un fantome, vous avez §3Faiblesse 1§r le jour et §9Speed 1§r la nuit.",
                "De plus, vous esquivez un coup §ltous les 10 coups§r subits."
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        if(event.getEpisode() == 3) {
            canBeGhost = true;
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!ghost) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());

        if(getUHCPlayer().equals(uhcPlayer)) {
            if(coups >= 10) {
                coups = 0;
                event.setCancelled(true);

                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez esquivé un coup !"));
            }
            else {
                coups++;
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(UHCPlayerDeathEvent event) {
        if(ghost) return;

        if(canBeGhost) {
            if(new Random().nextInt(100) <= 3) {
                ghost = true;
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous etes devenu un fantome !"));

                if(UHCAPI.getInstance().getGameHandler().getGameConfig().isDay()) {
                    getUHCPlayer().removeStrength(15);
                }
                else {
                    getUHCPlayer().addSpeed(20);
                }
            }
        }
    }

    @Override
    public void onDay() {
        if(ghost) {
            getUHCPlayer().removeStrength(15);
            getUHCPlayer().removeSpeed(20);
        }
    }

    @Override
    public void onNight() {
        if(ghost) {
            getUHCPlayer().addStrength(15);
            getUHCPlayer().addSpeed(20);
        }
    }

    private static class StoneWall extends TargetBlockItemPower {

        public StoneWall() {
            super(ChatColor.GRAY + "Mur de Pierre", Material.CLAY_BALL, 20, 15 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Place un mur de 9x9 sur le bloc visé."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Block target, Action clickAction) {
            Vector dir = player.getPlayer().getEyeLocation().getDirection().setY(0).normalize();
            Vector orthogonal = new Vector(-dir.getZ(), 0, dir.getX());

            new BukkitRunnable() {
                int y = target.getY();

                @Override
                public void run() {
                    if(y >= target.getY() + 9) {
                        cancel();
                        return;
                    }

                    for(int i = -5; i <= 5; i++) {
                        Block block = target.getWorld().getBlockAt(
                                (int) (target.getX() + i * orthogonal.getX()),
                                y,
                                (int) (target.getZ() + i * orthogonal.getZ()));
                        if(block.getType() == Material.AIR) {
                            if(new Random().nextDouble() < 0.2) block.setType(Material.COBBLESTONE);
                            else block.setType(Material.STONE);
                        }
                    }
                    y++;
                }
            }.runTaskTimer(NinjagoV2.getInstance(), 0, 2);

            return true;
        }
    }

    private static class EarthSpinjitzu extends SpinjitzuPower {

        public EarthSpinjitzu() {
            super(ChatColor.GRAY, Material.NETHER_STAR, 15 * 60, -1, new String[] {
                    "Vous obtenez 10 coeurs d'absorbtion pendant 2 minutes."
            });
        }

        @Override
        public void applyEffects(UHCPlayer player, List<UHCPlayer> players) {
            player.addPotionEffect(PotionEffectType.ABSORPTION, 2 * 60, 5);
        }
    }
}
