package me.butter.api.customEntities;

import net.minecraft.server.v1_8_R3.EntityCreature;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import org.bukkit.Location;

public interface CustomEntity {

    void init();

    void spawn(Location location);

    void onEntityDeath();

    Location getEntityLocation();

    EntityCreature getEntity();
}
