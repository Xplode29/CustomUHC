package me.butter.ninjago.roles.list.serpents;

import me.butter.api.module.power.TargetPlayerItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Lizaru extends NinjagoRole {

    private UHCPlayer acidicus;

    public Lizaru() {
        super("Lizaru", "/roles/serpent/lizaru", new Bite());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "A l'annonce des roles, vous obtenez le pseudo de §1Acidicus§r.",
        };
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Acidicus && role.getUHCPlayer() != null && role.getUHCPlayer().getPlayerState() == PlayerState.IN_GAME) {
                acidicus = role.getUHCPlayer();
            }
        }
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(acidicus == null ? "Il n'y a pas d'Acidicus dans cet partie" : "Acidicus:" + acidicus.getName()));
    }

    private static class Bite extends TargetPlayerItemPower {

        public Bite() {
            super("Morsure", Material.NETHER_STAR, 20, 5 * 60, -1);
        }

        public String[] getDescription() {
            return new String[] {
                    "Inflige 1.5 coeurs de degats et §2Poison 1§r pendant 5 secondes au joueur visé."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, Action clickAction) {
            target.getPlayer().setHealth(Math.max(0.5, target.getPlayer().getHealth() - 3.0));
            target.addPotionEffect(PotionEffectType.POISON, 5, 1);

            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez empoisonné " + target.getName()));
            return true;
        }
    }
}
