package me.butter.impl.game.configs;

import me.butter.api.game.configs.WorldConfig;

public class WorldConfigImpl implements WorldConfig {

    private boolean worldGenerated, pregenDone, borderMoving, netherActivated, enderActivated;

    private int appleDropRate, flintDropRate, enderPearlDropRate, diamondLimit, expBoost, startingBorderSize, finalBorderSize;

    public WorldConfigImpl() {
        this.worldGenerated = false;
        this.pregenDone = false;

        this.netherActivated = false;
        this.enderActivated = false;

        this.appleDropRate = 20;
        this.flintDropRate = 20;
        this.enderPearlDropRate = 20;
        this.diamondLimit = 0;
        this.expBoost = 100;

        this.borderMoving = false;
        this.startingBorderSize = 50;
        this.finalBorderSize = 50;
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
    public boolean isNetherActivated() {
        return netherActivated;
    }

    @Override
    public void setNetherActivated(boolean netherActivated) {
        this.netherActivated = netherActivated;
    }

    @Override
    public boolean isEnderActivated() {
        return enderActivated;
    }

    @Override
    public void setEnderActivated(boolean enderActivated) {
        this.enderActivated = enderActivated;
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
