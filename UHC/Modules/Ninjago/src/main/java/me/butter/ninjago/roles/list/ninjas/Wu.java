package me.butter.ninjago.roles.list.ninjas;

import com.google.common.collect.ImmutableMap;
import me.butter.api.UHCAPI;
import me.butter.api.module.power.EnchantedItemPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Wu extends NinjagoRole {

    private UHCPlayer ninja;

    public Wu() {
        super("Wu", "/roles/ninjas/wu", new StickPower(), new SpinjitzuPower());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous connaissez l'identité d'un ninja parmis §aLloyd§r, §aKai§r, §aJay§r, §aCole§r et §aZane§r"
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ninja == null ? "Aucun ninja trouve" : "Ninja : " + ninja.getName());
    }

    @Override
    public void onDistributionFinished() {
        List<UHCPlayer> pseudos = new ArrayList<>();
        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(uhcPlayer.getRole() instanceof Lloyd || uhcPlayer.getRole() instanceof Kai || uhcPlayer.getRole() instanceof Jay || uhcPlayer.getRole() instanceof Cole || uhcPlayer.getRole() instanceof Zane) {
                pseudos.add(uhcPlayer);
            }
        }

        if(!pseudos.isEmpty()) {
            ninja = pseudos.get((new Random()).nextInt(pseudos.size()));
        }
    }

    public static class StickPower extends EnchantedItemPower {
        public StickPower() {
            super("§eBaton", Material.DIAMOND_SWORD, ImmutableMap.of(Enchantment.DAMAGE_ALL, 4));
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Une épée en diamant Tranchant 4"
            };
        }
    }

    public static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super("§eSpinjitzu", Material.NETHER_STAR, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Repousse tous les joueurs dans un rayon de 10 blocks. Vous obtenez §eRegeneration 2§r pendant 30 secondes."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            Location center = player.getPlayer().getLocation();

            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                if(uhcPlayer == null || uhcPlayer == player || uhcPlayer.getPlayer() == null) continue;

                if(uhcPlayer.isNextTo(player, 10)) {
                    double angle = Math.atan2(uhcPlayer.getLocation().getZ() - center.getZ(), uhcPlayer.getLocation().getX() - center.getX());
                    Vector newVelocity = new Vector(
                            1.5 * Math.cos(angle),
                            0.5,
                            1.5 * Math.sin(angle)
                    );
                    uhcPlayer.getPlayer().setVelocity(newVelocity);
                }
            }

            player.addPotionEffect(PotionEffectType.REGENERATION, 30, 2);

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.YELLOW);
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez utilisé votre Spinjitzu !"));
            return true;
        }
    }
}
