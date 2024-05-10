package me.butter.api.game.configs;

public interface WorldConfig {

    boolean isWorldGenerated(); void setWorldGenerated(boolean worldGenerated);

    boolean isPregenDone(); void setPregenDone(boolean pregenDone);

    int getAppleDropRate(); void setAppleDropRate(int appleDropRate);

    int getFlintDropRate(); void setFlintDropRate(int flintDropRate);

    int getEnderPearlDropRate(); void setEnderPearlDropRate(int enderPearlDropRate);

    int getDiamondLimit(); void setDiamondLimit(int diamondLimit);

    boolean isBorderMoving(); void setBorderMoving(boolean borderMoving);

    int getStartingBorderSize(); void setStartingBorderSize(int borderSize);

    int getFinalBorderSize(); void setFinalBorderSize(int borderSize);

    int getExpBoost(); void setExpBoost(int expBoost);
}
