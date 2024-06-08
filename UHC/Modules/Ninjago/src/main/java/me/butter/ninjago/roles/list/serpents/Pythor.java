package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pythor extends NinjagoRole {

    EaterPower eaterPower;

    List<String> list = new ArrayList<>();

    public Pythor() {
        super("Pythor", "/roles/serpent/pythor", Collections.singletonList(new EaterPower()));
        for(Power power : getPowers()) {
            if(power instanceof EaterPower) {
                eaterPower = (EaterPower) power;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 et Speed 1 permanent, ainsi qu'une liste contenant les roles Skales, Acidicus, Skalidor, Fangtom, Fangdam, Arcturus et Ed0.",
                "",
                "Attention, Ed ne gagne pas avec les serpents.",
                "",
                "(Suite pas encore dev)",
                "Si vous obtenez les 4 armes d'or, que vous tuez au moins deux ninjas parmis Nya, Jay, Kai, Wu, Cole, Zane et qu'il reste 3 joueurs ou moins en vie parmis les serpents (vous inclus),",
                "",
                "Vous pouvez craft la forme finale du grand dévoreur (/ni craft gd pour voir le craft).",
                "",
                "Vous deviendrez alors solitaire, vous obtiendrez Speed 2, Resistance 1 et 15 coeurs permanents,",
                "",
                "Cependant, tous les joueurs de la partie seront informés que le Grand Dévoreur a été invoqué, sans mentionner votre pseudo."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(
                "Liste des serpents (Le role Ed fire dans la liste !) : \n" + String.join(", ", list)
        ));
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addStrength(20);
        getUHCPlayer().addSpeed(20);
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if (role instanceof NinjagoRole && role.getUHCPlayer() != null) {
                if(((NinjagoRole) role).isInList()) {
                    list.add(role.getUHCPlayer().getName());
                }
            }
        }
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        eaterPower.canUsePower = true;
    }

    private static class EaterPower extends RightClickItemPower {

        boolean canUsePower = true;

        public EaterPower() {
            super("Grand Devoreur", Material.NETHER_STAR, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "À l'activation, vous obtenez speed 2 et résistance 1 pendant 2 minutes",
                    "Ce pouvoir ne peut être active qu'une seule fois par episode"
            };
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(canUsePower) {
                player.addResi(20);
                player.addSpeed(20);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.removeResi(20);
                        player.removeSpeed(20);
                    }
                }.runTaskLater(Ninjago.getInstance(), 2 * 60 * 20);

                canUsePower = false;
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez activé votre forme de grand dévoreur !"));
                return true;
            }
            else {
                player.sendMessage(ChatUtils.ERROR.getMessage("Veuillez attendre l'episode suivant !"));
            }
            return false;
        }
    }
}
