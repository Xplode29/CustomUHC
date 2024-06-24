package me.butter.ninjago.roles.list.maitres;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.power.TargetBlockItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ParticleUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Ash extends NinjagoRole {

    private List<UHCPlayer> maitres;

    private final HideKillsCommand hideKillsCommand;
    private final SmokePower smokePower;

    private int coups = 0;

    public Ash() {
        super("Ash", "/roles/alliance-des-elements/ash");
        addPower(hideKillsCommand = new HideKillsCommand());
        addPower(smokePower = new SmokePower());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous conaissez la liste des membres de l'alliance"
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Membres de l'alliance: " + maitres.stream().map(UHCPlayer::getName).collect(Collectors.joining(", ")));
    }

    @Override
    public void onDistributionFinished() {
        maitres = new ArrayList<>();

        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role.getUHCPlayer() != null && role.getCamp().equals(CampEnum.MASTER.getCamp())) {
                maitres.add(role.getUHCPlayer());
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) return;
        if(!smokePower.isActive) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());

        if(getUHCPlayer().equals(uhcPlayer) && uhcPlayer.getLocation().distance(smokePower.center) <= 25) {
            if(coups >= 10) {
                coups = 0;
                event.setCancelled(true);

                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez esquive un coup !"));
            }
            else {
                coups++;
            }
        }
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(getUHCPlayer().equals(event.getKiller())) {
            if(hideKillsCommand.hideKills) {
                for(UHCPlayer uhcPlayer : maitres) {
                    uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("(Visible seulement par l'alliance) Mort de " + event.getVictim().getName()));
                    if(event.getVictim().getRole() != null) {
                        uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Son role était: " + event.getVictim().getRole().getName()));
                    }
                }
                event.showDeath = false;
            }
        }
    }

    private static class HideKillsCommand extends CommandPower {

        boolean hideKills = false;

        public HideKillsCommand() {
            super("Brouilleur", "brouille", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Active / désactive votre passif. Lorsqu'il est activé, vous cachez le message de mort des kills.",
                    "Cependant, les maitres seront toujours informés de la mort"
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            hideKills = !hideKills;
            if(hideKills) {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif activé"));
            }
            else {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif désactivé"));
            }
            return false;
        }
    }

    private static class SmokePower extends TargetBlockItemPower {

        boolean isActive;
        Location center;
        int timer = 2 * 60;
        boolean hasSpeed = false;

        int timeSinceTp = 0;

        public SmokePower() {
            super(ChatColor.GRAY + "Ecran de Fumée§r", Material.NETHER_STAR, 20, 15 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Recouvre de fumée une zone de 25 blocks de rayon pendant 2 minutes. (Cooldown: 15 minutes / Utilisation infinie)",
                    "Dans cette zone, vous possédez Speed 1 et vous esquivez un coup tous les 10 coups subits.",
                    "De plus, vous pouvez vous teleporter sur un bloc present dans la zone a l'aide d'un clic droit. (Cooldown: 10 secondes / Utilisation infinie)"
            };
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public void onUsePower(UHCPlayer player, Action clickAction) {
            if(isActive) {
                if(timeSinceTp + 10 > UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Vous devez attendre " + GraphicUtils.convertToAccurateTime(10 + timeSinceTp - UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) + " secondes avant de pouvoir utiliser ce pouvoir."));
                    return;
                }

                Block target = player.getPlayer().getTargetBlock((Set<Material>) null, getRange());
                if(target == null || target.getType() == Material.AIR) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Aucun bloc n'est visé."));
                    return;
                }

                timeSinceTp = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer();
                onEnable(player, target, clickAction);
            }
            else {
                super.onUsePower(player, clickAction);
            }
        }

        @Override
        public boolean onEnable(UHCPlayer player, Block target, Action clickAction) {
            if(isActive) {
                if(target.getLocation().distance(center) > 25) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Le bloc n'est pas dans la zone."));
                    return false;
                }

                player.getPlayer().teleport(
                        new Location(
                                target.getWorld(),
                                target.getX(),
                                target.getY() + 1,
                                target.getZ(),
                                player.getLocation().getYaw(),
                                player.getLocation().getPitch()
                        )
                );
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous vous etes teleporté sur le bloc."));
                return false;
            }
            else {
                center = player.getLocation();
                isActive = true;
                timer = 2 * 60;
                hasSpeed = false;
                new SmokeRunnable(player, center).runTaskTimer(Ninjago.getInstance(), 0, 20);

                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez posé votre ecran de fumée !"));
                return true;
            }
        }

        private class SmokeRunnable extends BukkitRunnable {
            UHCPlayer player;
            Location center;

            public SmokeRunnable(UHCPlayer player, Location center) {
                this.player = player;
                this.center = center;
            }

            @Override
            public void run() {
                if(isActive) {
                    if(timer <= 0) {
                        isActive = false;
                        if(hasSpeed && center.distance(player.getPlayer().getLocation()) > 25) {
                            player.removeSpeed(20);
                            hasSpeed = false;
                        }
                    }
                    else {
                        if(!hasSpeed && center.distance(player.getPlayer().getLocation()) <= 25) {
                            player.addSpeed(20);
                            hasSpeed = true;
                            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous entrez dans votre zone !"));
                        }
                        if(hasSpeed && center.distance(player.getPlayer().getLocation()) > 25) {
                            player.removeSpeed(20);
                            hasSpeed = false;
                            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous sortez de votre zone !"));
                        }

                        for(int i = -5; i < 5; i++) {
                            ParticleUtils.zoneEffect(player, center.clone().add(0, i, 0), 25, Color.GRAY);
                        }

                        timer--;
                        player.sendActionBar("§eEcran de fumée §8" + " §8[" + GraphicUtils.getProgressBar(2 * 60 - timer, 2 * 60, 10, '|', ChatColor.GREEN, ChatColor.GRAY) + "§8]");
                    }
                }
            }
        }
    }
}
