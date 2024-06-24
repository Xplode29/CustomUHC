package me.butter.ninjago.roles.list.solos;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.list.ninjas.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Morro extends NinjagoRole {

    UHCPlayer wu = null;
    Map<UHCPlayer, Integer> ninjasTimers;
    GhostPower ghostPower;
    FreezePower freezePower;

    public Morro() {
        super("Morro", "/roles/solitaires/morro", new FreezePower(), new GhostPower());

        for(Power power : getPowers()) {
            if(power instanceof GhostPower) ghostPower = (GhostPower) power;
        }
        ninjasTimers = new HashMap<>();
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous possédez Force 1 ainsi que 2 coeurs supplémentaires permanents.",
                "Vous connaissez l'identité de Wu. ",
                "Lorque vous restez 10 minutes à coté d'un ninja, vous êtes informé de son pseudo. ",
                "Vous obtenez des effets selon les ninjas tués:",
                ChatUtils.LIST_ELEMENT.getMessage("§9Jay§r: Vous obtenez speed 2"),
                ChatUtils.LIST_ELEMENT.getMessage("§cKai§r: Vous obtenez fire aspect sur votre épée et flame sur votre arc"),
                ChatUtils.LIST_ELEMENT.getMessage("§8Cole§r: Vous obtenez résistance 1"),
                ChatUtils.LIST_ELEMENT.getMessage("§3Zane§r: Vous obtenez un passif (/n freeze).")
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList(ChatUtils.PLAYER_INFO.getMessage(wu == null ? "Il n'y a pas de Wu dans cette partie" : "Wu: " + wu.getName()));
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addStrength(20);
        getUHCPlayer().addMaxHealth(4);

        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<UHCPlayer, Integer> entry : ninjasTimers.entrySet()) {
                    if(!getUHCPlayer().isNextTo(entry.getKey(), 20)) continue;
                    if(entry.getValue() < 10 * 60) {
                        entry.setValue(entry.getValue() + 1);
                    }
                    else {
                        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage(entry.getKey().getName() + "est un ninja."));
                        ninjasTimers.remove(entry.getKey());
                    }
                }
            }
        }.runTaskTimer(Ninjago.getInstance(), 0, 20);
    }

    @Override
    public void onDistributionFinished() {
        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role.getUHCPlayer() == null) continue;
            if(role instanceof Jay) ninjasTimers.put(role.getUHCPlayer(), 0);
            else if(role instanceof Kai) ninjasTimers.put(role.getUHCPlayer(), 0);
            else if(role instanceof Cole) ninjasTimers.put(role.getUHCPlayer(), 0);
            else if(role instanceof Zane) ninjasTimers.put(role.getUHCPlayer(), 0);

            if(role instanceof Wu) wu = role.getUHCPlayer();
        }
    }

    @EventHandler
    public void onHitPlayer(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        UHCPlayer damaged = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());

        if(damager.equals(getUHCPlayer()) && damaged != null) {
            if(freezePower != null && freezePower.freezeActivated) {
                if(new Random().nextInt(100) <= 5) {
                    damaged.removeSpeed(20);
                    Bukkit.getScheduler().runTaskLater(Ninjago.getInstance(), () -> damaged.addSpeed(20), 5 * 20);
                    getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ralenti " + damaged.getName() + " !"));
                }
            }

            if(!ghostPower.visible) {
                ghostPower.onUsePower(damager, Action.RIGHT_CLICK_AIR);
            }
        }
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(!event.getKiller().equals(getUHCPlayer())) return;
        if(event.getVictim().getRole() == null || !ninjasTimers.containsKey(event.getVictim())) return;

        Role role = event.getVictim().getRole();
        if(role instanceof Jay) {
            getUHCPlayer().addSpeed(40);
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu 40% de speed"));
        }
        if(role instanceof Kai) {
            FlameBow bow = new FlameBow();
            addPower(bow);
            getUHCPlayer().giveItem(bow.getItem(), true);

            FireAspectBook book = new FireAspectBook();
            addPower(book);
            getUHCPlayer().giveItem(book.getItem(), true);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez tué Kai ! /n role pour plus d'informations."));
        }
        if(role instanceof Cole) {
            getUHCPlayer().addResi(20);
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu 20% de resistance"));
        }
        if(role instanceof Zane) {
            freezePower = new FreezePower();
            addPower(freezePower);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez tué Zane ! /n role pour plus d'informations."));
        }
    }

    private static class FlameBow extends RightClickItemPower {

        public FlameBow() {
            super("Arc de flamme", Material.BOW, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"Un arc enchanté Power 3 Flame 1."};
        }

        @Override
        public ItemStack getItem() {
            return new ItemBuilder(Material.BOW)
                    .setName(getName())
                    .addEnchant(Enchantment.ARROW_DAMAGE, 3)
                    .addEnchant(Enchantment.ARROW_FIRE, 1)
                    .setUnbreakable()
                    .toItemStack();
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

    private static class FreezePower extends CommandPower {

        boolean freezeActivated = false;
        boolean hasZanePower = false;

        public FreezePower() {
            super("Freeze", "freeze", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Active / désactive votre passif (uniquement après avoir tué Zane). ",
                    "Lorqu'il est activé, vous avez 5% de chance d'infliger slowness 1 pendant 5 secondes lorsque vous tapez un joueur"
            };
        }

        @Override
        public boolean showPower() {
            return hasZanePower;
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            if(hasZanePower) {
                freezeActivated = !freezeActivated;
                if(freezeActivated) {
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif activé"));
                }
                else {
                    player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Passif désactivé"));
                }
            }
            else {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez pas tué Zane !"));
            }
            return false;
        }
    }

    private static class GhostPower extends RightClickItemPower {

        boolean visible = true;

        public GhostPower() {
            super(ChatColor.GREEN + "Fantome", Material.NETHER_STAR, 5 * 60, -1);
        }
        @Override
        public String[] getDescription() {
            return new String[]{"Active / désactive votre passif. Lorqu'il est activé, vous êtes invisible, même avec votre armure"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            visible = !visible;
            if(visible) {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous êtes maintenant visible"));
                for(Player p : Bukkit.getOnlinePlayers()) {
                    p.showPlayer(player.getPlayer());
                }
                return true;
            }
            else {
                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous êtes maintenant invisible"));
                for(UHCPlayer u : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                    u.getPlayer().hidePlayer(player.getPlayer());
                }
                return false;
            }
        }
    }
}
