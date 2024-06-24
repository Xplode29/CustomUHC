package me.butter.ninjago.roles.list.solos;

import me.butter.api.UHCAPI;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.goldenNinja.ChatEffectChooser;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.event.EventHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Skylor extends NinjagoRole {

    public Skylor() {
        super("Skylor", "/roles/solitaires/skylor");
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez Force et speed permanent",
                "A chaque début d'épisode, vous obtenez les effets et le pseudo d'un joueur aléatoire dans un rayon de 100 blocks. ",
                "A chaque kill, vous pouvez choisir 5% d'un effet de votre choix (avec maximum 40% par effet)"
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(20);
        getUHCPlayer().addStrength(20);
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        List<UHCPlayer> uhcPlayers = new ArrayList<>();

        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(uhcPlayer != null && getUHCPlayer().isNextTo(uhcPlayer, 100) && uhcPlayer.getRole() != null) {
                uhcPlayers.add(uhcPlayer);
            }
        }

        if(uhcPlayers.isEmpty()) {
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Il n'y a personne autour de vous"));
            return;
        }

        Collections.shuffle(uhcPlayers);

        UHCPlayer uhcPlayer = uhcPlayers.get(0);
        getUHCPlayer().sendMessage(ChatUtils.LIST_HEADER.getMessage("Voici les effets de " + uhcPlayer.getName()));
        for(Potion potion : uhcPlayer.getPotionEffects()) {
            getUHCPlayer().sendMessage(ChatUtils.LIST_ELEMENT.getMessage(potion.getEffect().getName()));
        }
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(!event.getKiller().equals(getUHCPlayer())) return;
        UHCAPI.getInstance().getClickableChatHandler().sendToPlayer(new ChatEffectChooser(getUHCPlayer(), 5, 40));
    }
}
