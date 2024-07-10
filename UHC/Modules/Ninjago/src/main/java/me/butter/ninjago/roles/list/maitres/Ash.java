package me.butter.ninjago.roles.list.maitres;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.TargetBlockItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ParticleEffects;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private int coups = 0, smokeTimer = 2 * 60;
    private boolean hideKills = false, isSmokePlaced = false, hasSpeed = false;
    private Location smokeCenter;

    int timeSinceTp = 0;

    public Ash() {
        super("Ash", "/roles/alliance-des-elements-4/ash");
        addPower(new HideKillsCommand());
        addPower(new SmokePower());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez No Fall permanent",
                "Vous connaissez la liste des membres de l'alliance"
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
    public void onGiveRole() {
        getUHCPlayer().setNoFall(true);
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
        if(!isSmokePlaced) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());

        if(getUHCPlayer().equals(uhcPlayer) && uhcPlayer.getLocation().distance(smokeCenter) <= 25) {
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
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(getUHCPlayer().equals(event.getKiller())) {
            if(hideKills) {
                for(UHCPlayer uhcPlayer : maitres) {
                    uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("(Visible seulement par l'alliance) Le joueur " + event.getVictim().getName() + "est mort."));
                    if(event.getVictim().getRole() != null) {
                        uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                                "Il était: " + event.getVictim().getRole().getCamp().getPrefix() + event.getVictim().getRole().getName()
                        ));
                    }
                }
                event.showDeath = false;
            }
        }
    }

    private class HideKillsCommand extends CommandPower {

        public HideKillsCommand() {
            super(ChatColor.DARK_GRAY + "Brouilleur", "brouille", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Lorsque ce passif est activé, vous cachez l'annonce de mort des joueurs que vous tuez.",
                    "Les membres de l'alliance seront informés du nom et du role du joueur mort."
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

    private class SmokePower extends TargetBlockItemPower {

        public SmokePower() {
            super(ChatColor.GRAY + "Ecran de Fumée", Material.NETHER_STAR, 20, 10 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Recouvre de fumée une zone de 30 blocks de rayon pendant 2 minutes. (Cooldown: 15 minutes / Utilisation infinie)",
                    "Dans cette zone, vous possédez §cForce 1§r et vous esquivez un coup tous les §l10 coups§r subits.",
                    "De plus, vous pouvez vous téléporter sur un bloc present dans la zone a l'aide d'un clic droit. (Cooldown: 10 secondes / Utilisation infinie)"
            };
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public void onUsePower(UHCPlayer player, Action clickAction) {
            if(isSmokePlaced) {
                if(timeSinceTp + 10 > UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Vous devez attendre " + GraphicUtils.convertToAccurateTime(10 + timeSinceTp - UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) + " secondes avant de pouvoir vous téléporter."));
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
            if(isSmokePlaced) {
                if(target.getLocation().distance(smokeCenter) > 25) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Le bloc visé n'est pas dans la zone."));
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
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous vous etes téléporté sur le bloc."));
                return false;
            }
            else {
                smokeCenter = player.getLocation();
                isSmokePlaced = true;
                smokeTimer = 2 * 60;
                hasSpeed = false;
                new SmokeRunnable(player, smokeCenter).runTaskTimer(Ninjago.getInstance(), 0, 20);

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
                if(isSmokePlaced) {
                    if(smokeTimer <= 0) {
                        isSmokePlaced = false;
                        if(hasSpeed && center.distance(player.getPlayer().getLocation()) > 30) {
                            player.removeStrength(15);
                            hasSpeed = false;
                        }
                    }
                    else {
                        if(!hasSpeed && center.distance(player.getPlayer().getLocation()) <= 30) {
                            player.addStrength(15);
                            hasSpeed = true;
                            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous entrez dans votre zone !"));
                        }
                        if(hasSpeed && center.distance(player.getPlayer().getLocation()) > 30) {
                            player.removeStrength(15);
                            hasSpeed = false;
                            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous sortez de votre zone !"));
                        }

                        for(int i = -5; i < 5; i++) {
                            ParticleEffects.zoneEffect(player, center.clone().add(0, i, 0), 30, Color.GRAY);
                        }

                        smokeTimer--;
                        player.sendActionBar("§eEcran de fumée §8" + " §8[" + GraphicUtils.getProgressBar(2 * 60 - smokeTimer, 2 * 60, 10, '|', ChatColor.GREEN, ChatColor.GRAY) + "§8]");
                    }
                }
            }
        }
    }
}
