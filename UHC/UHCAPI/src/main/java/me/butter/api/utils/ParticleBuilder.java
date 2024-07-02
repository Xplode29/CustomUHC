package me.butter.api.utils;

import me.butter.api.player.UHCPlayer;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;

import java.util.ArrayList;
import java.util.List;

public class ParticleBuilder {

    EnumParticle particle;
    Color color;
    Location loc;

    List<UHCPlayer> players;

    public ParticleBuilder(EnumParticle particle) {
        this.particle = particle;

        this.players = new ArrayList<>();
    }

    public ParticleBuilder setLocation(Location loc) {
        this.loc = loc;
        return this;
    }

    public ParticleBuilder setColor(Color color) {
        this.color = color;
        return this;
    }

    public ParticleBuilder addPlayer(UHCPlayer player) {
        this.players.add(player);
        return this;
    }

    public ParticleBuilder setPlayers(List<UHCPlayer> players) {
        this.players = players;
        return this;
    }

    public void build() {
        for(UHCPlayer player : players) {
            if(player.getPlayer() == null || player.getLocation().distance(loc) > 50.0 || !player.getLocation().getWorld().equals(loc.getWorld())) return;

            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles (
                    particle, true,
                    (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(),
                    (float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255,
                    (float) 1, 0
            );

            ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
