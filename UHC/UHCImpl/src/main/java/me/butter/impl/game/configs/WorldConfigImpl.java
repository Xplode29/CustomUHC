package me.butter.impl.game.configs;

import me.butter.api.game.configs.WorldConfig;

public class WorldConfigImpl implements WorldConfig {

    boolean worldGenerated, pregenDone, borderMoving;

    int appleDropRate, flintDropRate, enderPearlDropRate, diamondLimit, expBoost, startingBorderSize, finalBorderSize;

    public WorldConfigImpl() {
        this.worldGenerated = false;
        this.pregenDone = false;

        this.appleDropRate = 20;
        this.flintDropRate = 20;
        this.enderPearlDropRate = 20;
        this.diamondLimit = 22;
        this.expBoost = 50;

        this.borderMoving = false;
        this.startingBorderSize = 500;
        this.finalBorderSize = 100;
    }

    @Override
    public boolean isWorldGenerated() {
        return worldGenerated;
    }

    @Override
    public void setWorldGenerated(boolean worldGenerated) {
        this.worldGenerated = worldGenerated;
    }

    @Override
    public boolean isPregenDone() {
        return pregenDone;
    }

    @Override
    public void setPregenDone(boolean pregenDone) {
        this.pregenDone = pregenDone;
    }

    @Override
    public int getAppleDropRate() {
        return appleDropRate;
    }

    @Override
    public void setAppleDropRate(int appleDropRate) {
        this.appleDropRate = appleDropRate;
    }

    @Override
    public int getFlintDropRate() {
        return flintDropRate;
    }

    @Override
    public void setFlintDropRate(int flintDropRate) {
        this.flintDropRate = flintDropRate;
    }

    @Override
    public int getEnderPearlDropRate() {
        return enderPearlDropRate;
    }

    @Override
    public void setEnderPearlDropRate(int enderPearlDropRate) {
        this.enderPearlDropRate = enderPearlDropRate;
    }

    @Override
    public int getDiamondLimit() {
        return diamondLimit;
    }

    @Override
    public void setDiamondLimit(int diamondLimit) {
        this.diamondLimit = diamondLimit;
    }

    @Override
    public boolean isBorderMoving() {
        return borderMoving;
    }

    @Override
    public void setBorderMoving(boolean borderMoving) {
        this.borderMoving = borderMoving;
    }

    @Override
    public int getStartingBorderSize() {
        return startingBorderSize;
    }

    @Override
    public void setStartingBorderSize(int borderSize) {
        this.startingBorderSize = borderSize;
    }

    @Override
    public int getFinalBorderSize() {
        return finalBorderSize;
    }

    @Override
    public void setFinalBorderSize(int borderSize) {
        this.finalBorderSize = borderSize;
    }

    @Override
    public int getExpBoost() {
        return expBoost;
    }

    @Override
    public void setExpBoost(int expBoost) {
        this.expBoost = expBoost;
    }
}
