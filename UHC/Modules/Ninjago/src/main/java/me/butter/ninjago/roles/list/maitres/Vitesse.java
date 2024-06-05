package me.butter.ninjago.roles.list.maitres;

import me.butter.api.module.power.CommandPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vitesse extends NinjagoRole {

    List<String> maitres;

    public Vitesse() {
        super("Griffin Turner (Maitre de la vitesse)", "doc", Collections.singletonList(new SpeedCommand()));
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Vous conaissez la liste des maitres"};
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Maitres: " + String.join(", ", maitres));
    }

    @Override
    public void onDistributionFinished() {
        maitres = new ArrayList<>();

        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role.getUHCPlayer() != null && role.getCamp().equals(CampEnum.MASTER.getCamp())) {
                maitres.add(role.getUHCPlayer().getName());
            }
        }
    }

    private static class SpeedCommand extends CommandPower {

        public SpeedCommand() {
            super("Vitesse", "vitesse", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Vous pouvez choisir votre pourcentage de vitesse, entre 0% et 50%"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            if(args.length < 2) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Usage: /ni vitesse <nombre>"));
                return false;
            }

            int speed = Integer.parseInt(args[1]);
            if(0 <= speed &&  speed <= 50) {
                player.addSpeed(speed - player.getSpeed());
            }
            else {
                player.sendMessage(ChatUtils.ERROR.getMessage("Veuillez entrer un nombre entre 0 et 50"));
            }

            return false;
        }
    }
}
