package me.butter.ninjago.roles.list.maitres;

import me.butter.api.module.power.ItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.NinjagoV2;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Karlof extends NinjagoRole {

    private List<String> maitres;
    private boolean steelActive = false;
    private int steelTimer = 3 * 60;

    public Karlof() {
        super("Karlof", "/roles/alliance-des-elements/karlof");
        addPower(new SteelPower());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez §cForce 1§r permanent.",
                "Vous connaissez la liste des membres de l'alliance."
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Membres de l'alliance: " + String.join(", ", maitres));
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addStrength(15);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(steelActive) {
                    steelTimer--;

                    if(steelTimer <= 0) {
                        steelActive = false;
                        getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez plus assez de temps, votre blindage s'est désactivé !"));
                    }
                }
            }
        }.runTaskTimer(NinjagoV2.getInstance(), 0, 20);
    }

    @Override
    public void onDistributionFinished() {
        maitres = new ArrayList<>();

        for(Role role : NinjagoV2.getInstance().getRolesList()) {
            if(role.getUHCPlayer() != null && role.getCamp().equals(CampEnum.MASTER.getCamp())) {
                maitres.add(role.getUHCPlayer().getName());
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(UHCPlayerDeathEvent event) {
        if(getUHCPlayer().equals(event.getKiller())) {
            steelTimer += 30;
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu 30 secondes de Blindage supplémentaires."));
        }
    }

    private class SteelPower extends ItemPower {

        public SteelPower() {
            super(ChatColor.DARK_GRAY + "Blindage", Material.NETHER_STAR, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Lorsque cet item est activé, vous obtenez §7Resistance 2§r mais vous perdez §cForce 1§r et vous obtenez §7Lenteur 1§r. ",
                    "Vous avez 3 minutes d'utilisation maximum. ",
                    "Lorsque vous faites un kill, vous obtenez 30 secondes d'utilisation supplémentaires",
                    "Vous pouvez vérifier le temps qu'il vous reste avec un clic gauche."
            };
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(steelTimer <= 0) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez plus assez de temps !"));
                return false;
            }

            if(clickAction == Action.RIGHT_CLICK_AIR || clickAction == Action.RIGHT_CLICK_BLOCK) {
                steelActive = !steelActive;
                if(steelActive) {
                    player.addResi(40);

                    player.removeStrength(15);
                    player.removeSpeed(20);
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Blindage activé"));
                }
                else {
                    player.removeResi(40);

                    player.addStrength(15);
                    player.addSpeed(20);
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Blindage désactivé"));
                }
            }
            else {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Il vous reste " + GraphicUtils.convertToAccurateTime(steelTimer) + " d'utilisation"));
            }
            return false;
        }
    }
}
