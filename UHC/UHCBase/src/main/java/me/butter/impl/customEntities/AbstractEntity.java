package me.butter.impl.customEntities;

import me.butter.api.customEntities.CustomEntity;
import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public abstract class AbstractEntity implements CustomEntity {

    EntityCreature entity;
    World world;

    public AbstractEntity(EntityCreature entity) {
        this.entity = entity;

        init();
    }

    @Override
    public void init() {

    }

    public void spawn(Location location) {
        entity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        world = ((CraftWorld) location.getWorld()).getHandle();
        world.addEntity(entity, CreatureSpawnEvent.SpawnReason.CUSTOM);

        for (Player player : location.getWorld().getPlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(entity));
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(entity.getId(), entity.getDataWatcher(), true));
        }
    }

    @Override
    public void onEntityDeath() {

    }

    @Override
    public Location getEntityLocation() {
        return entity.getBukkitEntity().getLocation();
    }

    @Override
    public EntityCreature getEntity() {
        return entity;
    }
}
