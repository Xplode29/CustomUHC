package me.butter.impl.game.configs;

import me.butter.api.game.configs.ItemConfig;

public class ItemConfigImpl implements ItemConfig {

    private boolean rod, enderPearl, bow, projectile, lavaBucket, flintAndSteel;

    public ItemConfigImpl() {
        this.rod = false;
        this.enderPearl = false;
        this.bow = true;
        this.projectile = false;
        this.lavaBucket = true;
        this.flintAndSteel = false;
    }

    @Override
    public boolean isRod() {
        return rod;
    }

    @Override
    public void setRod(boolean rod) {
        this.rod = rod;
    }

    @Override
    public boolean isEnderPearl() {
        return enderPearl;
    }

    @Override
    public void setEnderPearl(boolean enderPearl) {
        this.enderPearl = enderPearl;
    }

    @Override
    public boolean isBow() {
        return bow;
    }

    @Override
    public void setBow(boolean bow) {
        this.bow = bow;
    }

    @Override
    public boolean isProjectile() {
        return projectile;
    }

    @Override
    public void setProjectile(boolean projectile) {
        this.projectile = projectile;
    }

    @Override
    public boolean isLavaBucket() {
        return lavaBucket;
    }

    @Override
    public void setLavaBucket(boolean lavaBucket) {
        this.lavaBucket = lavaBucket;
    }

    @Override
    public boolean isFlintAndSteel() {
        return flintAndSteel;
    }

    @Override
    public void setFlintAndSteel(boolean flintAndSteel) {
        this.flintAndSteel = flintAndSteel;
    }
}
