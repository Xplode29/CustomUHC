package me.butter.ninjago.items.goldenWeapons;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import me.butter.ninjago.roles.list.ninjas.Zane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

public class IceShuriken extends AbstractGoldenWeapon implements Listener {

    public IceShuriken() {
        super("Shurikens de glace", Material.SNOW_BALL, 5 * 60);
    }

    @Override
    public boolean onEnable() {
        Player p = getHolder().getPlayer();
        if(p == null) return false;

        Snowball ball = getHolder().getPlayer().getWorld().spawn(p.getEyeLocation(), Snowball.class);
        ball.setShooter(p);
        ball.setVelocity(p.getLocation().getDirection().multiply(1.5));

        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous utilisez les shurikens de glace !"));
        return true;
    }

    @EventHandler
    public void onPlayerHitBySnowball(EntityDamageByEntityEvent event) {
         if(event.getDamager() instanceof Snowball) {
             Snowball snowBall = (Snowball) event.getDamager();
            if(!(snowBall.getShooter() instanceof Player)) return;

            UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) snowBall.getShooter());
            if(!player.equals(getHolder())) return;

            UHCPlayer victim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
            if(victim == null) return;

            victim.addPotionEffect(PotionEffectType.SLOW, 10, 1);
            event.setCancelled(true);
        }
    }

    @Override
    public void onPickup() {
        getHolder().addMaxHealth(2);
        if(getHolder().getRole() instanceof Zane || getHolder().getRole() instanceof Lloyd) getHolder().addMaxHealth(2);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ramassé les shurikens de glace !"));
    }

    @Override
    public void onDrop() {
        getHolder().removeMaxHealth(2);
        if(getHolder().getRole() instanceof Zane || getHolder().getRole() instanceof Lloyd) getHolder().removeMaxHealth(2);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez laché les shurikens de glace !"));
    }
}
