package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.roles.Role;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.NinjagoV2;
import me.butter.ninjago.goldenNinja.ChatEffectChooser;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Apprenti extends NinjagoRole {

    private boolean hasMaster = false, isKaiMaster = false, isZaneMaster = false, isWuMaster = false;
    private UHCPlayer master;

    public Apprenti() {
        super("Apprenti", "/roles/ninjas/apprenti-du-dojo");
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Lorsque vous passez pour la première fois à moins de 5 blocks d'un des roles suivants, vous deviendrez son élève. Vous obtendrez donc son pseudo ainsi que les capacités suivantes :",
                " §3Nya§r: Vous obtenez un livre §bDepth Strider 3§r",
                " §9Jay§r: Vous obtenez §920% de speed§r à 20 blocks de lui, et §930% de speed§r permanent à sa mort",
                " §cKai§r: Vous obtenez §c10% de force§r ainsi que 10% de chances d'enflammer le joueur frappé (si vous possédez le §cSabre de feu§r, vous enflammez à coup sur le joueur frappé)",
                " §aLloyd§r: Vous obtenez le choix entre §915% de speed§r, §c15% de force§r ou §715% de resistance§r.",
                " §8Cole§r: Vous obtenez §710% de résistance§r à 20 blocks de lui, et §720% de résistance§r permanent à sa mort",
                " §bZane§r: Vous obtenez §l1 coeur§r permanent supplémentaire ainsi qu'§lun autre coeur§r permanent à sa mort",
                " §eWu§r: Vous obtenez l'accès au §eSpinjitzu§r. Comme Wu, vous obtenez §eRegeneration 2§r pendant 30 secondes.",
        };
    }

    @Override
    public void onDistributionFinished() {
        List<UHCPlayer> models = new ArrayList<>();

        for(Role role : NinjagoV2.getInstance().getRolesList()) {
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
            new SearchMasterRunnable(models).runTaskTimer(NinjagoV2.getInstance(), 20, 20);
        }
    }

    private void setMaster(UHCPlayer player) {
        hasMaster = true;
        this.master = player;

        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Votre nouveau maitre est " + master.getName()));

        if(master.getRole() instanceof Nya) {
            Nya.DepthStriderBook book = new Nya.DepthStriderBook();
            addPower(book);
            getUHCPlayer().giveItem(book.getItem(), true);
        }
        else if(master.getRole() instanceof Jay) {
            new JayMasterRunnable(master).runTaskTimer(NinjagoV2.getInstance(), 20, 20);
        }
        else if(master.getRole() instanceof Kai) {
            getUHCPlayer().addStrength(10);
            isKaiMaster = true;
        }
        else if(master.getRole() instanceof Lloyd) {
            UHCAPI.getInstance().getClickableChatHandler().sendToPlayer(new ChatEffectChooser(getUHCPlayer(), 15, -1));
        }
        else if(master.getRole() instanceof Cole) {
            new ColeMasterRunnable(master).runTaskTimer(NinjagoV2.getInstance(), 20, 20);
        }
        else if(master.getRole() instanceof Zane) {
            getUHCPlayer().addMaxHealth(2);
            isZaneMaster = true;
        }
        else if(master.getRole() instanceof Wu) {
            Wu.SpinjitzuPower power = new Wu.SpinjitzuPower();
            addPower(power);
            getUHCPlayer().giveItem(power.getItem(), true);
            isWuMaster = true;

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu l'accès au Spinjitzu !"));
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof LivingEntity) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        if(damager.equals(getUHCPlayer())) {
            if(hasMaster && isKaiMaster) {
                if(new Random().nextInt(100) <= 10 || getUHCPlayer().equals(NinjagoV2.getInstance().getGoldenWeaponManager().fireSaber.getHolder())) {
                    event.getEntity().setFireTicks(20 * 3);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDie(UHCPlayerDeathEvent event) {
        if(!hasMaster || !event.getVictim().equals(master)) return;
        if(isZaneMaster) getUHCPlayer().addMaxHealth(2);
        if(isWuMaster) {
            Wu.StickPower stick = new Wu.StickPower();
            addPower(stick);
            getUHCPlayer().giveItem(stick.getItem(), true);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu le baton de Wu !"));
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
}
