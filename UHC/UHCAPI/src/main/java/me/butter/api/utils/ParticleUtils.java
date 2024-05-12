package me.butter.api.utils;

import me.butter.api.UHCAPI;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class ParticleUtils {
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

                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                            EnumParticle.REDSTONE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) 1, 0
                    );
                    for(Entity entity : player.getNearbyEntities(50, 50, 50)) {
                        if(entity instanceof Player) {
                            ((CraftPlayer)entity).getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                    ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
                }
                height += 0.2;

                if(height >= 2.0) {
                    cancel();
                }
            }
        }.runTaskTimer(UHCAPI.get(), 0, 1);
    }

    public static void slicingEffect(Player player, int amount, Color color) {
        Location location = player.getLocation().clone();

        Vector lookVector = player.getLocation().getDirection().normalize();
        Vector rightHeadVector = new Vector(lookVector.getZ(), 0, -lookVector.getX()).normalize();

        for(double i = 0; i < amount; i++) {
            double angle = (Math.PI / 3) * (i / amount) + (11 * Math.PI / 6);

            Vector vector = new Vector(
                    lookVector.getX() * Math.cos(angle) + rightHeadVector.getX() * Math.sin(angle),
                    lookVector.getY() * Math.cos(angle) + rightHeadVector.getY() * Math.sin(angle),
                    lookVector.getZ() * Math.cos(angle) + rightHeadVector.getZ() * Math.sin(angle)
            ).normalize().multiply(4);

            float x = (float) (vector.getX() + location.getX());
            float y = (float) (vector.getY() + location.getY()) + 1.5f;
            float z = (float) (vector.getZ() + location.getZ());

            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                    EnumParticle.REDSTONE, true, x, y, z, (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, (float) 1, 0
            );
            for(Entity entity : player.getNearbyEntities(50, 50, 50)) {
                if(entity instanceof Player) {
                    ((CraftPlayer)entity).getHandle().playerConnection.sendPacket(packet);
                }
            }
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
        }
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
