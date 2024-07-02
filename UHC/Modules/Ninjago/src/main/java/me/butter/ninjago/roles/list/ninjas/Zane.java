package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleEffects;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.*;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Zane extends NinjagoRole {

    public Zane() {
        super("Zane", "/roles/ninjas/zane", new SpinjitzuPower(), new TraqueCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Vous avez §l2 coeurs§r permanents."};
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addMaxHealth(4);
    }

    private static class TraqueCommand extends TargetCommandPower {

        UHCPlayer target;

        public TraqueCommand() {
            super("Traque", "traque", 10 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Traque le joueur ciblé pendant 2 minutes"
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            this.target = target;

            new BukkitRunnable() {
                int timer = 2 * 60;

                @Override
                public void run() {
                    if(player == null || player.getPlayer() == null || target == null || target.getPlayer() == null) return;

                    if(timer <= 0) {
                        player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("La traque de " + target.getPlayer().getName() + " est terminée"));
                        cancel();
                        return;
                    }

                    Location eye = player.getPlayer().getEyeLocation().clone().add(0, -1.0, 0);
                    Vector eyeDirection = eye.getDirection().setY(0);
                    Vector toEntity = target.getPlayer().getEyeLocation().clone().toVector().setY(0).subtract(eye.toVector().setY(0));
                    double dot = toEntity.normalize().dot(eyeDirection);
                    double otherDot = eyeDirection.getZ() * toEntity.getX() - eyeDirection.getX() * toEntity.getZ();

                    String dotArrow = "";
                    if(dot > 0.5) dotArrow = "▲";
                    else if(dot < -0.5) dotArrow = "▼";
                    else {
                        if(otherDot > 0) dotArrow = "◀";
                        if(otherDot < 0) dotArrow = "▶";
                    }

                    player.sendActionBar("§l" + dotArrow);

                    timer--;
                }
            }.runTaskTimer(Ninjago.getInstance(), 0, 20);

            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez traque " + target.getPlayer().getName()));
            return true;
        }
    }

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.AQUA + "Spinjitzu", Material.NETHER_STAR, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Repousse tous les joueurs dans un rayon de 10 blocks. Vous ralentissez tous les joueurs touchés pendant 1 minute."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            Location center = player.getPlayer().getLocation();

            List<UHCPlayer> slowed = new ArrayList<>();

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

                    slowed.add(uhcPlayer);
                }
            }

            for(UHCPlayer u : slowed) {
                u.addPotionEffect(PotionEffectType.SLOW, 60, 1);
                u.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous ete ralenti par Zane !"));
            }

            ParticleEffects.tornadoEffect(player.getPlayer(), Color.WHITE);
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous utilisez votre Spinjitzu !"));
            return true;
        }
    }
}