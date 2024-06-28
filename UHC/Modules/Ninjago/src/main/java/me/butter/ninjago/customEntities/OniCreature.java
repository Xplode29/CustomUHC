package me.butter.ninjago.customEntities;

import me.butter.impl.customEntities.AbstractEntity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R3.PathfinderGoalNearestAttackableTarget;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class OniCreature extends AbstractEntity {
    public OniCreature(World world) {
        super(new EntityIronGolem(((CraftWorld) world).getHandle()));
    }

    @Override
    public void init() {
        getEntity().setCustomName("Oni");
        getEntity().setCustomNameVisible(true);

        getEntity().goalSelector.a(0, new PathfinderGoalMeleeAttack(getEntity(), 1.0D, false));
        getEntity().targetSelector.a(0, new PathfinderGoalNearestAttackableTarget<>(getEntity(), EntityHuman.class, true));
    }
}
