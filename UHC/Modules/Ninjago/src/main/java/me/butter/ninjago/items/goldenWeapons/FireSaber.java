package me.butter.ninjago.items.goldenWeapons;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.roles.list.ninjas.Kai;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Random;

public class FireSaber extends AbstractGoldenWeapon implements Listener {
    public FireSaber() {
        super("Sabre de feu", Material.BLAZE_ROD, 0);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;

        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getDamager());
        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
        if(damager == null || target == null || target.getPlayer() == null) return;

        if(damager.equals(getHolder())) {
            if((new Random()).nextInt(100) <= 10) {
                target.getPlayer().setFireTicks(60);
            }
        }
    }

    @Override
    public void onPickup() {
        getHolder().addStrength(5);
        if(getHolder().getRole() instanceof Kai || getHolder().getRole() instanceof Lloyd) getHolder().addStrength(5);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ramassé le sabre de feu !"));
    }

    @Override
    public void onDrop() {
        getHolder().removeStrength(5);
        if(getHolder().getRole() instanceof Kai || getHolder().getRole() instanceof Lloyd) getHolder().removeStrength(5);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez laché le sabre de feu !"));
    }
}
