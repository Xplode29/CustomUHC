package me.butter.ninjago.roles.list.serpents;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

public class Skales extends NinjagoRole {

    private UHCPlayer pythor;

    private static int timeToInfect = 7 * 60;

    public Skales() {
        super("Skales", "/roles/serpent-10/skales", new InfectPower());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez §cForce 1§r la nuit. ",
                "A l'annonce des roles, vous obtenez le pseudo de §5Pythor§r."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de Pythor" : "Pythor: " + pythor.getName()));
    }

    @Override
    public void onGiveRole() {
        if(!UHCAPI.getInstance().getGameHandler().getGameConfig().isDay()) {
            getUHCPlayer().addStrength(15);
        }
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Pythor && role.getUHCPlayer() != null) {
                pythor = role.getUHCPlayer();
                break;
            }
        }
    }

    @Override
    public boolean isInList() {
        return true;
    }

    @Override
    public void onDay() {
        getUHCPlayer().removeStrength(15);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addStrength(15);
    }

    public static class InfectPower extends TargetCommandPower {

        boolean infected = false;

        public InfectPower() {
            super("Infection", "infect", 0, 1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "En restant pendant 7 minutes à 12 blocs du joueur ciblé, vous pouvez le faire rejoindre votre camp. ",
                    "Vous ne pouvez pas infecter les §eSolitaires§r et §cLloyd§r. Vous ne serez pas informé si l'infection a échoué. "
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            new InfectRunnable(player, target);
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez lancé votre infection sur " + target.getName()));
            return true;
        }

        private class InfectRunnable extends BukkitRunnable {

            UHCPlayer player, target;
            int timer;

            public InfectRunnable(UHCPlayer player, UHCPlayer target) {
                this.player = player;
                this.target = target;

                this.runTaskTimer(Ninjago.getInstance(), 0, 20);
            }

            @Override
            public void run() {
                if(target.getPlayer() == null || player.getPlayer() == null) return;

                float progress = (float) timer / timeToInfect;
                player.sendActionBar(GraphicUtils.getProgressBar((int) (progress * 100), 100, 20, '|', ChatColor.GREEN, ChatColor.GRAY));

                if(player.isNextTo(target, 12)) {
                    timer++;
                }

                if(timer >= timeToInfect) {
                    if(!(
                        target.getRole().getCamp() == CampEnum.DUO_LLOYD_GARMADON.getCamp() ||
                        target.getRole().getCamp() == CampEnum.SOLO.getCamp() ||
                        target.getRole() instanceof Lloyd
                    )) { //Réussie
                        target.getRole().setCamp(CampEnum.SNAKE.getCamp());
                        target.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez été infecté par Skales (" + player.getName() + "). Vous devez maintenant gagner avec les " + ChatColor.DARK_BLUE + "Serpents§r."));
                    }

                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("L'infection de " + target.getPlayer().getName() + " est terminée."));
                    infected = true;
                    cancel();
                }
            }
        }
    }
}
