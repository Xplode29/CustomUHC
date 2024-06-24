package me.butter.ninjago.goldenWeapons.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.goldenWeapons.AbstractGoldenWeapon;
import me.butter.ninjago.roles.list.ninjas.Jay;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class LightingNunchakus extends AbstractGoldenWeapon implements Listener {

    public LightingNunchakus() {
        super("Nunchakus electriques", Material.STICK, 0);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
        if(damager == null || target == null || target.getPlayer() == null) return;

        if(damager.equals(getHolder())) {
            if((new Random()).nextInt(100) <= 10) {
                target.getPlayer().getWorld().strikeLightningEffect(target.getLocation());
                target.getPlayer().playSound(target.getPlayer().getLocation(), Sound.EXPLODE, 1.0f, 1.0f);
                target.getPlayer().setHealth(Math.max(0.5, target.getPlayer().getHealth() - 2.0));
            }
        }
    }

    @Override
    public void onPickup() {
        getHolder().addSpeed(5);
        if(getHolder().getRole() instanceof Jay || getHolder().getRole() instanceof Lloyd) getHolder().addSpeed(5);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ramassé les Nunchakus electriques !"));
    }

    @Override
    public void onDrop() {
        getHolder().removeSpeed(5);
        if(getHolder().getRole() instanceof Jay || getHolder().getRole() instanceof Lloyd) getHolder().removeSpeed(5);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez laché les Nunchakus electriques !"));
    }
}
