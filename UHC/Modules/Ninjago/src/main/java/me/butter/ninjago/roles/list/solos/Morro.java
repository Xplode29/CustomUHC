package me.butter.ninjago.roles.list.solos;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.list.ninjas.*;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.WorldSettings;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Morro extends NinjagoRole {

    private UHCPlayer wu = null;
    private final Map<UHCPlayer, Integer> ninjasTimers;
    private boolean visible = true, freezeActivated = false, hasZanePower = false;
    private final GhostPower ghostPower;

    public Morro() {
        super("Morro", "/roles/solitaires/morro");
        addPower(ghostPower = new GhostPower());

        ninjasTimers = new HashMap<>();
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous possédez §cForce 1§r ainsi que §l2 coeurs§r supplémentaires permanents.",
                "Vous connaissez l'identité de §aWu§r. ",
                "Lorsque vous restez 10 minutes à coté d'un ninja, vous êtes informé de son pseudo. ",
                "Vous obtenez des effets selon les ninjas tués :",
                " §9Jay§r: Vous obtenez §9Speed 2§r.",
                " §cKai§r: Vous obtenez son §c§lEpée des Flammes§r et son §c§lArc des Flammes§r",
                " §8Cole§r: Vous obtenez §7Résistance 1§r.",
                " §3Zane§r: Vous obtenez un passif (/n freeze)."
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
        getUHCPlayer().addStrength(15);
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
            if(freezeActivated) {
                if(new Random().nextInt(100) <= 5) {
                    damaged.addPotionEffect(PotionEffectType.SLOW, 10, 1);
                    getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ralenti " + damaged.getName() + " !"));
                }
            }

            if(!visible) {
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
            Kai.FlameBow bow = new Kai.FlameBow();
            addPower(bow);
            getUHCPlayer().giveItem(bow.getItem(), true);

            Kai.FlameSword book = new Kai.FlameSword();
            addPower(book);
            getUHCPlayer().giveItem(book.getItem(), true);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez tué Kai ! /n role pour plus d'informations."));
        }
        if(role instanceof Cole) {
            getUHCPlayer().addResi(20);
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu 20% de resistance"));
        }
        if(role instanceof Zane) {
            addPower(new FreezePower());
            hasZanePower = true;
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez tué Zane ! /n role pour plus d'informations."));
        }
    }

    private class FreezePower extends CommandPower {

        public FreezePower() {
            super("Freeze", "freeze", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Active / désactive votre passif (uniquement après avoir tué Zane). ",
                    "Lorsqu'il est activé, vous avez 5% de chance d'infliger §2Lenteur 1§r pendant 10 secondes lorsque vous tapez un joueur"
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

    private class GhostPower extends RightClickItemPower {

        public GhostPower() {
            super(ChatColor.GREEN + "Fantome", Material.NETHER_STAR, 5 * 60, -1);
        }
        @Override
        public String[] getDescription() {
            return new String[]{
                    "Active / désactive votre passif. Lorqu'il est activé, vous êtes invisible, même avec votre armure."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(player.getPlayer() == null) return false;

            visible = !visible;
            if(visible) {
                for(UHCPlayer u : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                    if(u.getPlayer() == null || u == getUHCPlayer()) continue;

                    PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(
                            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                            ((CraftPlayer) player.getPlayer()).getHandle()
                    );
                    info.new PlayerInfoData(
                            ((CraftPlayer) player.getPlayer()).getProfile(),
                            1,
                            WorldSettings.EnumGamemode.valueOf(player.getPlayer().getGameMode().name()),
                            IChatBaseComponent.ChatSerializer.a(player.getName())
                    );
                    ((CraftPlayer) u.getPlayer()).getHandle().playerConnection.sendPacket(info);

                    u.getPlayer().showPlayer(player.getPlayer());
                }

                player.removePotionEffect(PotionEffectType.INVISIBILITY);

                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous êtes maintenant visible"));
                return true;
            }
            else {
                for(UHCPlayer u : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                    if(u.getPlayer() == null || u == getUHCPlayer()) continue;

                    u.getPlayer().hidePlayer(player.getPlayer());

                    PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(
                            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                            ((CraftPlayer) player.getPlayer()).getHandle()
                    );
                    info.new PlayerInfoData(
                            ((CraftPlayer) player.getPlayer()).getProfile(),
                            1,
                            WorldSettings.EnumGamemode.valueOf(player.getPlayer().getGameMode().name()),
                            IChatBaseComponent.ChatSerializer.a(player.getName())
                    );
                    ((CraftPlayer) u.getPlayer()).getHandle().playerConnection.sendPacket(info);
                }

                player.addPotionEffect(PotionEffectType.INVISIBILITY, -1, 1);

                player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous êtes maintenant invisible"));
                return false;
            }
        }
    }
}
