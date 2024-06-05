package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.power.Power;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Skales extends NinjagoRole {

    UHCPlayer pythor;

    InfectPower infectionPower;

    public Skales() {
        super("Skales", "/roles/serpent/skales", Arrays.asList(new InfectPower()));
        for(Power power : getPowers()) {
            if(power instanceof InfectPower) {
                infectionPower = (InfectPower) power;
                break;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez force 1 la nuit. ",
                "A l'annonce des roles, vous obtenez le pseudo de Pythor."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de Pythor" : "Pythor:" + pythor.getName()));
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
        getUHCPlayer().removeStrength(20);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addStrength(20);
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
                    "Vous ne pouvez pas infecter les solitaires et Lloyd. Vous ne serez pas informé si l'infection a échoué. "
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

            int timeToInfect = 60;

            public InfectRunnable(UHCPlayer player, UHCPlayer target) {
                this.player = player;
                this.target = target;

                this.runTaskTimer(Ninjago.getInstance(), 0, 20);
            }

            @Override
            public void run() {
                float progress = (float) timer / timeToInfect;
                player.sendActionBar(GraphicUtils.getProgressBar((int) (progress * 100), 100, 20, '|', ChatColor.GREEN, ChatColor.GRAY));

                if(player.isNextTo(target, 12)) {
                    timer++;
                }

                if(timer >= timeToInfect) {
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("L'infection de " + target.getPlayer().getName() + " est terminée."));

                    if(!(target.getRole().getCamp() == CampEnum.SOLO.getCamp() || target.getRole() instanceof Lloyd)) { //Réussie
                        target.getRole().setCamp(CampEnum.SNAKE.getCamp());
                        target.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez été infecté par Skales (" + player.getName() + "). Vous devez maintenant gagner avec les " + ChatColor.DARK_BLUE + "Serpents§r."));
                    }
                    infected = true;
                    cancel();
                }
            }
        }
    }
}
