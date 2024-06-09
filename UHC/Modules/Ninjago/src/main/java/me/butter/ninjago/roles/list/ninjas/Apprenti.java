package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.EnchantBookPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.items.SpinjitzuPower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Apprenti extends NinjagoRole {

    boolean hasMaster = false, isKaiMaster, isLloydMaster, isColeMaster, isJayMaster, isWuMaster;
    UHCPlayer master;

    public Apprenti() {
        super("Apprenti", "/roles/ninjas/apprenti-du-dojo", Collections.emptyList());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                ChatUtils.LIST_HEADER + "Lorsque vous passez pour la première fois à moins de 5 blocks d'un des roles suivants, vous deviendrez son élève. Vous obtendrez donc son pseudo ainsi que les capacités suivantes :",
                "",
                ChatUtils.LIST_ELEMENT + "§3Nya§r: Vous obtenez un livre Depth Strider 3",
                "",
                ChatUtils.LIST_ELEMENT + "§1Jay§r: Vous obtenez 20% de speed à 20 blocks de lui, et 30% de speed permanent à sa mort",
                "",
                ChatUtils.LIST_ELEMENT + "§cKai§r: Vous obtenez 10% de force ainsi que 10% de chances d'enflammer le joueur frappé (si vous possédez le Sabre de feu, vous enflammez à coup sur le joueur frappé)",
                "",
                ChatUtils.LIST_ELEMENT + "§aLloyd§r: Vous obtenez 2 coeurs permanent supplémentaires ainsi qu'un autre coeur permanent à sa mort",
                "",
                ChatUtils.LIST_ELEMENT + "§8Cole§r: Vous obtenez 10% de résistance à 20 blocks de lui, et 20% de résistance permanent à sa mort",
                "",
                ChatUtils.LIST_ELEMENT + "§bZane§r: Vous obtenez No Fall et 10% de speed permanent",
                "",
                ChatUtils.LIST_ELEMENT + "§eWu§r: Vous obtenez l'accès au Spinjitzu"
        };
    }

    @Override
    public void onDistributionFinished() {
        List<UHCPlayer> models = new ArrayList<>();

        for(Role role : Ninjago.getInstance().getRolesList()) {
            if(role instanceof Nya && role.getUHCPlayer() != null) models.add(role.getUHCPlayer());
            else if(role instanceof Jay && role.getUHCPlayer() != null) models.add(role.getUHCPlayer());
            else if(role instanceof Kai && role.getUHCPlayer() != null) models.add(role.getUHCPlayer());
            else if(role instanceof Lloyd && role.getUHCPlayer() != null) models.add(role.getUHCPlayer());
            else if(role instanceof Cole && role.getUHCPlayer() != null) models.add(role.getUHCPlayer());
            else if(role instanceof Zane && role.getUHCPlayer() != null) models.add(role.getUHCPlayer());
            else if(role instanceof Wu && role.getUHCPlayer() != null) models.add(role.getUHCPlayer());
        }

        if (models.isEmpty()) {
            getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Désolé, mais vous n'aurez pas de maitre dans cette partie !"));
        }
        else {
            new SearchMasterRunnable(models).runTaskTimer(Ninjago.getInstance(), 20, 20);
        }
    }

    private void setMaster(UHCPlayer player) {
        hasMaster = true;
        this.master = player;

        if(master.getRole() instanceof Nya) {
            DepthStriderBook book = new DepthStriderBook();
            addPower(book);
            getUHCPlayer().giveItem(book.getItem(), true);
        }
        else if(master.getRole() instanceof Jay) {
            new JayMasterRunnable(master).runTaskTimer(Ninjago.getInstance(), 20, 20);
            isJayMaster = true;
        }
        else if(master.getRole() instanceof Kai) {
            getUHCPlayer().addStrength(10);
            isKaiMaster = true;
        }
        else if(master.getRole() instanceof Lloyd) {
            getUHCPlayer().addMaxHealth(4);
            isLloydMaster = true;
        }
        else if(master.getRole() instanceof Cole) {
            new ColeMasterRunnable(master).runTaskTimer(Ninjago.getInstance(), 20, 20);
            isColeMaster = true;
        }
        else if(master.getRole() instanceof Zane) {
            getUHCPlayer().addSpeed(10);
            getUHCPlayer().setNoFall(true);
        }
        else if(master.getRole() instanceof Wu) {
            addPower(new SpinjitzuPower(ChatColor.AQUA));
            isWuMaster = true;
        }

        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Votre nouveau maitre est " + master.getName()));
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        if(damager.equals(getUHCPlayer())) {
            if(hasMaster && isKaiMaster) {
                if(new Random().nextInt(100) <= 10) {
                    event.getEntity().setFireTicks(20 * 3);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDie(UHCPlayerDeathEvent event) {
        if(!hasMaster || !event.getVictim().equals(master)) return;
        if(isLloydMaster) getUHCPlayer().addMaxHealth(2);
        if(isWuMaster) {
            StickPower stick = new StickPower();
            addPower(stick);
            getUHCPlayer().giveItem(stick.getItem(), true);
        }
    }

    private class SearchMasterRunnable extends BukkitRunnable {
        List<UHCPlayer> models;

        public SearchMasterRunnable(List<UHCPlayer> models) {
            this.models = models;
        }

        @Override
        public void run() {
            if (getUHCPlayer().getPlayer() == null) {
                return;
            }

            for(UHCPlayer model : models) {
                if(getUHCPlayer().isNextTo(model, 5)) {
                    setMaster(model);

                    cancel();
                    return;
                }
            }
        }
    }

    private class JayMasterRunnable extends BukkitRunnable {
        UHCPlayer master;
        boolean nextToJay;

        public JayMasterRunnable(UHCPlayer master) {
            this.master = master;
        }

        @Override
        public void run() {
            if(getUHCPlayer() == null) {
                return;
            }

            if(master.getPlayerState() == PlayerState.DEAD) {
                if(nextToJay) {
                    getUHCPlayer().removeSpeed(20);
                    nextToJay = false;
                }
                getUHCPlayer().addSpeed(30);
                cancel();
                return;
            }

            if(getUHCPlayer().isNextTo(master, 20) && !nextToJay) {
                getUHCPlayer().addSpeed(20);
                nextToJay = true;
            }
            else if(!getUHCPlayer().isNextTo(master, 20) && nextToJay) {
                getUHCPlayer().removeSpeed(20);
                nextToJay = false;
            }
        }
    }

    private class ColeMasterRunnable extends BukkitRunnable {
        UHCPlayer master;
        boolean nextToCole;

        public ColeMasterRunnable(UHCPlayer master) {
            this.master = master;
        }

        @Override
        public void run() {
            if(getUHCPlayer() == null) {
                return;
            }

            if(master.getPlayerState() == PlayerState.DEAD) {
                if(nextToCole) {
                    getUHCPlayer().removeResi(10);
                    nextToCole = false;
                }
                getUHCPlayer().addResi(20);
                cancel();
                return;
            }

            if(getUHCPlayer().isNextTo(master, 20) && !nextToCole) {
                getUHCPlayer().addResi(10);
                nextToCole = true;
            }
            else if(!getUHCPlayer().isNextTo(master, 20) && nextToCole) {
                getUHCPlayer().removeResi(10);
                nextToCole = false;
            }
        }
    }

    private static class DepthStriderBook extends EnchantBookPower {
        public DepthStriderBook() {
            super("§rLivre Depth Strider 3", Enchantment.DEPTH_STRIDER, 3);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Un livre enchanté Depth Strider 3. Il est possible de le fusionner avec une piece en diamant."
            };
        }

        @Override
        public boolean hidePower() {
            return true;
        }
    }

    private static class StickPower extends RightClickItemPower {

        public StickPower() {
            super("§eBaton", Material.DIAMOND_SWORD, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Une épée en diamant sharpness 4"
            };
        }

        @Override
        public boolean doesCancelEvent() {
            return false;
        }

        @Override
        public boolean hidePower() {
            return true;
        }

        @Override
        public ItemStack getItem() {
            ItemStack goldenSword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta itemMeta = goldenSword.getItemMeta();

            itemMeta.spigot().setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
            itemMeta.setDisplayName("§r" + getName());
            goldenSword.setItemMeta(itemMeta);
            return goldenSword;
        }
    }
}
