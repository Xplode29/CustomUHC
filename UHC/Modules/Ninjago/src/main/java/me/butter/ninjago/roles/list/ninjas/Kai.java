package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.ParticleUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class Kai extends NinjagoRole {

    boolean nextToNya;

    public Kai() {
        super("Kai", "doc", Arrays.asList(
                new FireAspectBook(),
                new SpinjitzuPower()
        ));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Fire Resistance permanent ainsi que 10% de force a 10 blocs de nya",
                "A l'annonce des roles, vous obtenez un Arc power 3 flame 1"
        };
    }

    @Override
    public boolean isNinja() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addPotionEffect(PotionEffectType.FIRE_RESISTANCE, -1, 1);

        getUHCPlayer().giveItem(new ItemBuilder(Material.BOW)
                .addEnchant(Enchantment.ARROW_DAMAGE, 3)
                .addEnchant(Enchantment.ARROW_FIRE, 1)
                .setUnbreakable()
                .toItemStack(), true);
    }

    @Override
    public void onDistributionFinished() {
        UHCPlayer nya = null;
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Nya && role.getUHCPlayer() != null) {
                nya = role.getUHCPlayer();
            }
        }

        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage(Ninjago.getInstance().getRolesList().toString()));

        if (nya != null) {
            UHCPlayer finalNya = nya;
            Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
                if(getUHCPlayer() == null) {
                    return;
                }

                if(finalNya.getLocation().distance(getUHCPlayer().getLocation()) <= 10 && !nextToNya) {
                    getUHCPlayer().addStrength(10);
                    nextToNya = true;
                }
                else if(finalNya.getLocation().distance(getUHCPlayer().getLocation()) > 10 && nextToNya) {
                    getUHCPlayer().removeStrength(10);
                    nextToNya = false;
                }
            }, 20, 20);
        }
    }

    private static class FireAspectBook extends EnchantBookPower {
        public FireAspectBook() {
            super("Livre Fire Aspect", Enchantment.FIRE_ASPECT, 1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Un livre enchanté fire aspect 1. Il est possible de le fusionner avec son épée en diamant."};
        }
    }

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.RED + "Spinjitzu", Material.NETHER_STAR, 5 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {"À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            List<Entity> nearbyEntities = player.getPlayer().getNearbyEntities(4, 2, 4);
            Location center = player.getPlayer().getLocation();
            for(Entity entity : nearbyEntities) {
                double angle = Math.atan2(entity.getLocation().getZ() - center.getZ(), entity.getLocation().getX() - center.getX());
                Vector newVelocity = new Vector(
                        1.5 * Math.cos(angle),
                        0.5, //* Math.signum(entity.getLocation().getY() - center.getY()),
                        1.5 * Math.sin(angle)
                );
                entity.setVelocity(newVelocity);
            }

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.RED);

            return true;
        }
    }
}
