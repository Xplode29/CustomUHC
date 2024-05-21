package me.butter.api.game.configs;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface ItemConfig {

    boolean isRod(); void setRod(boolean rod);

    boolean isEnderPearl(); void setEnderPearl(boolean enderPearl);

    boolean isBow(); void setBow(boolean bow);

    boolean isProjectile(); void setProjectile(boolean projectile);

    boolean isLavaBucket(); void setLavaBucket(boolean lavaBucket);

    boolean isFlintAndSteel(); void setFlintAndSteel(boolean flintAndSteel);
}
