package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.power.TargetItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Lizaru extends NinjagoRole {

    UHCPlayer acidicus;

    public Lizaru() {
        super("Lizaru", "/roles/serpent/lizaru", Collections.singletonList(new Bite()));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A l'annonce des roles, vous obtenez le pseudo de Skalidor.",
                "Lorsque vous effectuez un kill, vous obtenez 3% de force supplémentaire"
        };
    }

    @Override
    public List<String> additionalDescription() {
        if(acidicus == null || acidicus.getPlayerState() != PlayerState.IN_GAME) {
            for (Role role : Ninjago.getInstance().getRolesList()) {
                if(role instanceof Acidicus && role.getUHCPlayer() != null && role.getUHCPlayer().getPlayerState() == PlayerState.IN_GAME) {
                    acidicus = role.getUHCPlayer();
                    break;
                }
            }
        }

        if(acidicus == null) return Collections.emptyList();
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage("Acidicus:" + acidicus.getName()));
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller() != null && event.getKiller().equals(getUHCPlayer())) {
            getUHCPlayer().addStrength(3);
        }
    }

    private static class Bite extends TargetItemPower {

        public Bite() {
            super("Morsure", Material.NETHER_STAR, 20, 10, -1);
        }

        public String[] getDescription() {
            return new String[]{"Inflige poison pendant 10 secondes au joueur visé (Distance maximale: 20 blocks)"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, Action clickAction) {
            target.addPotionEffect(PotionEffectType.POISON, 10, 1);
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez empoisonne " + target.getName()));
            return true;
        }
    }
}
