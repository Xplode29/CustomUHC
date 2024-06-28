package me.butter.ninjago.roles.list.serpents;

import com.google.common.collect.ImmutableMap;
import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.EnchantedItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

public class Acidicus extends NinjagoRole {

    private UHCPlayer pythor;
    private int coups = 0;
    private boolean veninActive = false;
    private final CrochetSword crochetSword;

    public Acidicus() {
        super("Acidicus", "/roles/serpent/acidicus");
        addPower(new VeninCommand());
        addPower(crochetSword = new CrochetSword());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous n'êtes pas affecté par les effets négatifs (poison, faiblesse, lenteur, mining fatigue). ",
                "Vous conaissez l'identité de Pythor"
        };
    }

    @Override
    public void onGiveRole() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(getUHCPlayer().hasPotionEffect(PotionEffectType.POISON))
                    getUHCPlayer().removePotionEffect(PotionEffectType.POISON);
                if(getUHCPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING))
                    getUHCPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
                if(getUHCPlayer().hasPotionEffect(PotionEffectType.WEAKNESS))
                    getUHCPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
                if(getUHCPlayer().getSpeed() < 0) {
                    getUHCPlayer().addSpeed(-getUHCPlayer().getSpeed());
                }
            }
        }.runTaskTimer(Ninjago.getInstance(), 0, 20);
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Pythor && role.getUHCPlayer() != null) {
                pythor = role.getUHCPlayer();
                break;
            }
        }
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(pythor == null ? "Pas de Pythor" : "Pythor:" + pythor.getName()));
    }

    @Override
    public boolean isInList() {
        return true;
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        UHCPlayer damaged = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());

        if(damager.equals(getUHCPlayer()) && damaged != null && ((Player) event.getDamager()).getItemInHand().isSimilar(crochetSword.getItem())) {
            if(veninActive) {
                coups++;
                if(coups % 20 == 0) {
                    damaged.addPotionEffect(PotionEffectType.POISON, 5, 1);
                }
                if(coups % 30 == 0) {
                    damaged.removeStrength(15);
                    Bukkit.getScheduler().runTaskLater(Ninjago.getInstance(), () -> damaged.addStrength(15), 10 * 20);
                }
                if(coups % 50 == 0) {
                    damaged.removeSpeed(20);
                    Bukkit.getScheduler().runTaskLater(Ninjago.getInstance(), () -> damaged.addSpeed(20), 10 * 20);
                }
            }
        }
    }

    private static class CrochetSword extends EnchantedItemPower {

        public CrochetSword() {
            super("Crochet", Material.DIAMOND_SWORD, ImmutableMap.of(Enchantment.DAMAGE_ALL, 3));
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Lorsque vous tapez un joueur avec, vous infligez des effets en fonction ud nombre de coups",
                    ChatUtils.LIST_ELEMENT + "Tous les 20 coups, vous infligez Poison 1 pendant 5 secondes.",
                    ChatUtils.LIST_ELEMENT + "Tous les 30 coups, vous infligez Faiblesse 1 pendant 10 secondes.",
                    ChatUtils.LIST_ELEMENT + "Tous les 50 coups, vous infligez Lenteur 1 pendant 10 secondes.",
                    "Ces effets sont cumulables."
            };
        }
    }

    private class VeninCommand extends CommandPower {

        public VeninCommand() {
            super("Venin", "venin", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Active / désactive la capacite de votre epee Crochet."
            };
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            veninActive = !veninActive;
            if(veninActive) {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif activé"));
            }
            else {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif désactivé"));
            }
            return false;
        }
    }
}
