package me.butter.ninjago.goldenNinja;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.impl.scenario.AbstractScenario;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.list.ninjas.*;
import me.butter.ninjago.roles.list.solos.Garmadon;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GoldenNinjaScenario extends AbstractScenario {

    UHCPlayer goldenNinja;

    public GoldenNinjaScenario() {
        super("Ninja d'or", Material.GOLD_CHESTPLATE);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Au 3eme episode, un joueur",
                "est choisi comme ninja d'or."
        };
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        if(event.getEpisode() == 3) {
            List<UHCPlayer> players = new ArrayList<>(UHCAPI.getInstance().getPlayerHandler().getPlayersInGame());
            Collections.shuffle(players, new Random());

            for(UHCPlayer uhcPlayer : players) {
                if(uhcPlayer.getRole() instanceof NinjagoRole) {
                    NinjagoRole role = (NinjagoRole) uhcPlayer.getRole();
                    if(
                        role instanceof Wu ||
                        role instanceof Lloyd ||
                        role instanceof Kai ||
                        role instanceof Jay ||
                        role instanceof Cole ||
                        role instanceof Zane  ||
                        role instanceof Garmadon
                    ) {
                        goldenNinja = uhcPlayer;
                        UHCAPI.getInstance().getClickableChatHandler().sendToPlayer(new ChatEffectChooser(goldenNinja, 10, -1));
                        break;
                    }
                }
            }

            if(goldenNinja == null) return;
            for(UHCPlayer uhcPlayer : players) {
                if(uhcPlayer.getRole() instanceof NinjagoRole) {
                    NinjagoRole role = (NinjagoRole) uhcPlayer.getRole();
                    if(role.getStartCamp() != CampEnum.MASTER.getCamp()) {
                        uhcPlayer.sendMessage(ChatUtils.LINE.prefix);
                        uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage(goldenNinja.getName() + " est le ninja d'or !"));
                        uhcPlayer.sendMessage(ChatUtils.LINE.prefix);
                    }
                    uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Le ninja d'or a été annoncé"));
                }
            }
        }
    }
}
