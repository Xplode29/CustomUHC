package me.butter.ninjago.roles.list.maitres;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Fumee extends NinjagoRole {

    List<UHCPlayer> maitres;

    private HideKillsCommand power;

    public Fumee() {
        super("Ash (Maitre de la fumee)", "doc", Arrays.asList(new HideKillsCommand(), new SmokePower()));
        for(Power power : getPowers()) {
            if(power instanceof HideKillsCommand) {
                this.power = (HideKillsCommand) power;
                break;
            }
        }
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Maitres: " + maitres.stream().map(UHCPlayer::getName).collect(Collectors.joining(", ")));
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addStrength(20);
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
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(getUHCPlayer().equals(event.getKiller())) {
            if(power.hideKills) {
                for(UHCPlayer uhcPlayer : maitres) {
                    uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("(Visible seulement par les maitres) Mort de " + event.getVictim().getName() + " par " + event.getKiller().getName()));
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

    private static class SmokePower extends RightClickItemPower {

        SmokeZoneCooldown smokeZoneCooldown;

        private boolean smokePlaced;
        private Location smokeCoords;
        private int zoneTimer = 2 * 60;

        public SmokePower() {
            super(ChatColor.GRAY + "Ecran de Fumée§r", Material.NETHER_STAR, 10 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"À l'activation, vous recouvrez de fumée une zone de 25 blocks de rayon. Dans cette zone, les joueurs ne peuvent pas voir les maitres à plus de 7 blocks"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action action) {
            if(smokePlaced) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez déjà placé votre zone !"));
                return false;
            }

            if(smokeZoneCooldown == null) {
                smokeZoneCooldown = new SmokeZoneCooldown(player);
            }
            else {
                smokeZoneCooldown.reset();
            }

            smokeCoords = player.getLocation();
            smokePlaced = true;
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez placé votre zone !"));

            return true;
        }

        private class SmokeZoneCooldown extends BukkitRunnable {
            int timer;
            boolean isRunning;

            UHCPlayer player;

            public SmokeZoneCooldown(UHCPlayer player) {
                timer = 0;
                isRunning = true;

                this.player = player;

                this.runTaskTimer(Ninjago.getInstance(), 0, 20);
            }

            @Override
            public void run() {
                if(isRunning) {
                    if(timer > zoneTimer) {
                        isRunning = false;
                        smokePlaced = false;
                        for (Role role : Ninjago.getInstance().getRolesList()) {
                            if (role.getCamp() != CampEnum.MASTER.getCamp()) continue;
                            if (role.getUHCPlayer() == null) continue;
                            if (role.getUHCPlayer().getPlayer() == null) continue;

                            UHCPlayer master = role.getUHCPlayer();
                            for(Player p : Bukkit.getOnlinePlayers()) {
                                p.showPlayer(master.getPlayer());
                            }
                        }
                        player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Votre zone s'est effacée..."));
                    }
                    else {
                        for (Role role : Ninjago.getInstance().getRolesList()) {
                            if (role.getCamp() != CampEnum.MASTER.getCamp()) continue;
                            if (role.getUHCPlayer() == null) continue;
                            if (role.getUHCPlayer().getPlayer() == null) continue;

                            Location playerLoc = role.getUHCPlayer().getLocation();
                            UHCPlayer master = role.getUHCPlayer();

                            if (playerLoc.distance(smokeCoords) <= 25) {
                                for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                                    if(uhcPlayer.getPlayer() == null) continue;
                                    if(uhcPlayer.getRole().getCamp() == CampEnum.MASTER.getCamp()) continue;

                                    if(uhcPlayer.isNextTo(master, 7)) {
                                        uhcPlayer.getPlayer().showPlayer(master.getPlayer());
                                    }
                                    else {
                                        uhcPlayer.getPlayer().hidePlayer(master.getPlayer());
                                    }
                                }
                            }
                            else {
                                for(Player p : Bukkit.getOnlinePlayers()) {
                                    p.showPlayer(master.getPlayer());
                                }
                            }
                        }
                    }
                    timer++;
                }
            }

            public void reset() {
                this.timer = 0;
                this.isRunning = true;
            }
        }
    }
}
