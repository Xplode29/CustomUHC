package me.butter.ninjago.items;

import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleUtils;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import java.util.List;

public class SpinjitzuPower extends RightClickItemPower {

    public SpinjitzuPower(ChatColor color) {
        super(color + "Spinjitzu", Material.NETHER_STAR, 5 * 60, -1);
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