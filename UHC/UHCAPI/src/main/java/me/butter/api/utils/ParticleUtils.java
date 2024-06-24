package me.butter.api.utils;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils {

    public static void showParticleToPlayer(UHCPlayer player, Location loc, EnumParticle particle, Color color) {
        if(player.getPlayer() == null) return;

        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                EnumParticle.REDSTONE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) 1, 0
        );

        ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    public static void showParticleToPlayers(List<UHCPlayer> players, Location loc, EnumParticle particle, Color color) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                EnumParticle.REDSTONE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) 1, 0
        );

        for(UHCPlayer player : players) {
            if(player.getPlayer() == null) continue;
            ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static void zoneEffect(UHCPlayer player, Location center, int size, Color color) {
        for(int i = 0; i < 64; i++) {
            double alpha = i * (Math.PI / 32);
            Location loc = center.add(
                    size * Math.cos(alpha),
                    0,
                    size * Math.sin(alpha)
            );
            showParticleToPlayer(player, loc, EnumParticle.REDSTONE, color);
        }
    }

    public static void tornadoEffect(Player player, Color color) {
        new BukkitRunnable() {
            double height = 0;

            public void run() {
                for(int i = 0; i < 32; i++) {
                    double alpha = i * (Math.PI / 16);

                    Location loc = player.getLocation().add(
                            (0.5 + height) * Math.cos(alpha) / 2,
                            height,
                            (0.5 + height) * Math.sin(alpha) / 2
                    );

                    showParticleToPlayers(UHCAPI.getInstance().getPlayerHandler().getPlayers(), loc, EnumParticle.REDSTONE, color);
                }
                height += 0.2;

                if(height >= 2.0) {
                    cancel();
                }
            }
        }.runTaskTimer(UHCAPI.getInstance(), 0, 1);
    }

    private static Vector rotateAroundAxisX(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double y, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        y = v.getY() * cos - v.getZ() * sin;
        z = v.getY() * sin + v.getZ() * cos;
        return v.setY(y).setZ(z);
    }

    private static Vector rotateAroundAxisY(Vector v, double angle) {
        angle = -angle;
        angle = Math.toRadians(angle);
        double x, z, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos + v.getZ() * sin;
        z = v.getX() * -sin + v.getZ() * cos;
        return v.setX(x).setZ(z);
    }

    private static Vector rotateAroundAxisZ(Vector v, double angle) {
        angle = Math.toRadians(angle);
        double x, y, cos, sin;
        cos = Math.cos(angle);
        sin = Math.sin(angle);
        x = v.getX() * cos - v.getY() * sin;
        y = v.getX() * sin + v.getY() * cos;
        return v.setX(x).setY(y);
    }
}
