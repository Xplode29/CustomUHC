package me.butter.ninjago.roles.list.ninjas;

import com.google.common.collect.ImmutableMap;
import me.butter.api.UHCAPI;
import me.butter.api.module.power.EnchantedItemPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.Action;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Kai extends NinjagoRole {

    public Kai() {
        super("Kai", "/roles/ninjas/kai", new FlameBow(), new FlameSword(), new SpinjitzuPower());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Fire Resistance permanent",
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addPotionEffect(PotionEffectType.FIRE_RESISTANCE, -1, 1);
    }

    private static class FlameBow extends EnchantedItemPower {

        public FlameBow() {
            super("Arc de flamme", Material.BOW, ImmutableMap.of(Enchantment.ARROW_DAMAGE, 3, Enchantment.ARROW_FIRE, 1));
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Un arc enchanté Power 2 Flame 1."
            };
        }
    }

    private static class FlameSword extends EnchantedItemPower {
        public FlameSword() {
            super("Epee des flammes", Material.DIAMOND_SWORD, ImmutableMap.of(Enchantment.DAMAGE_ALL, 3, Enchantment.FIRE_ASPECT, 1));
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Une epee enchantee Fire Aspect 1."
            };
        }
    }

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.RED + "Spinjitzu", Material.NETHER_STAR, 20 * 60, -1);
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "À l'activation, repousse tous les joueurs dans un rayon de 10 blocks.",
                    "vous obtenez 10% de force pendant 2 minutes."
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

            player.addStrength(10);
            Bukkit.getScheduler().runTaskLater(Ninjago.getInstance(), () -> player.removeStrength(10), 2 * 60 * 20);

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.RED);
            return true;
        }
    }
}
