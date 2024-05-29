package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.Enchant;
import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ParticleUtils;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class Lloyd extends NinjagoRole {

    private GoldenSwordPower goldenSwordPower;

    public Lloyd() {
        super("Lloyd", "soon", Arrays.asList(
                new GoldenSwordPower(),
                new ProtectionBook(),
                new SpinjitzuPower()
        ));

        goldenSwordPower = null;
        for(Power power : getPowers()) {
            if(power instanceof GoldenSwordPower) {
                goldenSwordPower = (GoldenSwordPower) power;
                return;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Force 1 (20%) ainsi que 13 coeurs permanents"
        };
    }

    @Override
    public boolean isNinja() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addStrength(20);
        getUHCPlayer().getPlayer().setMaxHealth(26);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        LivingEntity targetPlayer = (LivingEntity) event.getEntity();
        if(damager.equals(getUHCPlayer()) && damager.getPlayer().getItemInHand().isSimilar(goldenSwordPower.getItem()) && goldenSwordPower.explosionNextHit) {
            targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.EXPLODE, 3.0f, 1.0f);
            targetPlayer.getWorld().playEffect(targetPlayer.getLocation().add(0, 1, 0), Effect.EXPLOSION_LARGE, 1);

            targetPlayer.setHealth(Math.max(0, targetPlayer.getHealth() - 4.0));
            goldenSwordPower.explosionNextHit = false;
            goldenSwordPower.forceCooldown();
            event.setDamage(0);
        }
    }

    private static class ProtectionBook extends EnchantBookPower {
        public ProtectionBook() {
            super("§rLivre Protection 3", Enchantment.PROTECTION_ENVIRONMENTAL, 3);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Un livre enchanté protection 3. Il est possible de le fusionner avec une piece en diamant."};
        }
    }

    private static class GoldenSwordPower extends RightClickItemPower {

        public boolean explosionNextHit = false;

        private boolean isCharging = false;

        public GoldenSwordPower() {
            super("§6Golden Sword", Material.DIAMOND_SWORD, 5 * 60, 2);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "En restant appuyé sur votre clic droit, vous chargez une barre d'énergie. ",
                    "Lorsqu'elle est pleine, le prochain coup créera une explosion infligeant 2 coeurs a la cible"
            };
        }

        @Override
        public boolean doesCancelEvent() {
            return false;
        }

        @Override
        public ItemStack getItem() {
            ItemStack goldenSword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta itemMeta = goldenSword.getItemMeta();

            itemMeta.spigot().setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
            itemMeta.setDisplayName("§6Golden Sword");
            goldenSword.setItemMeta(itemMeta);
            return goldenSword;
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(!isCharging) {
                new LoadRunnable(player);
                isCharging = true;
            }
            return false;
        }

        private class LoadRunnable extends BukkitRunnable {
            UHCPlayer uhcPlayer;
            int timer;

            public LoadRunnable(UHCPlayer player) {
                this.uhcPlayer = player;
                this.timer = 0;
                this.runTaskTimer(UHCAPI.getInstance(), 0, 1);
            }

            @Override
            public void run() {
                if(uhcPlayer.getPlayer() == null) {
                    cancel();
                    return;
                }
                if(timer < 3 * 20) {
                    if(!uhcPlayer.getPlayer().isBlocking()) {
                        uhcPlayer.sendMessage(ChatUtils.ERROR.getMessage("Vous avez relaché trop tot !"));
                        isCharging = false;
                        cancel();
                        return;
                    }
                    timer++;
                    float progress = (timer / (20f * 3));
                    uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_STICKS, 3.0F, (float)Math.pow(2.0, ((double)(progress) * 12 - 12.0) / 12.0));
                    uhcPlayer.sendActionBar(GraphicUtils.getProgressBar((int) (progress * 100), 100, 20, '|', ChatColor.GREEN, ChatColor.GRAY));
                }
                else {
                    uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_PLING, 3.0F, 6.0f);
                    uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Le prochain coup fera exploser le joueur frappé"));
                    explosionNextHit = true;
                    isCharging = false;
                    cancel();
                }
            }
        }
    }

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.GREEN + "Spinjitzu", Material.NETHER_STAR, 5 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {"À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 5 blocks"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            List<Entity> nearbyEntities = player.getPlayer().getNearbyEntities(5, 5, 5);
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

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.GREEN);

            return true;
        }
    }
}
