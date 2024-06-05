package me.butter.ninjago.roles.list.maitres;

import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Metal extends NinjagoRole {

    List<UHCPlayer> maitres;
    int masters = 0;

    public Metal() {
        super("Karlof (Maitre du métal)", "doc", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez force (15%) et résistance (40%) permanent",
                "Pour chaque maitre présent dans un rayon de 40 blocks, vous perdez 5% de force et de résistance",
                "Vous conaissez la liste des maitres"
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Maitres: " + maitres.stream().map(UHCPlayer::getName).collect(Collectors.joining(", ")));
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addStrength(15);
        getUHCPlayer().addResi(40);

        Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
            int count = 0;
            for(UHCPlayer uhcPlayer : maitres) {
                if(getUHCPlayer().isNextTo(uhcPlayer, 40)) {
                    count++;
                }
            }
            int diff = count - masters;
            if(masters != count) {
                if(diff > 0) {
                    getUHCPlayer().removeStrength(diff * 5);
                    getUHCPlayer().removeResi(diff * 5);
                } else if(diff < 0) {
                    getUHCPlayer().addStrength(-diff * 5);
                    getUHCPlayer().addResi(-diff * 5);
                }
                masters = count;
            }
        }, 0, 20);
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
}
