package me.butter.impl;

import me.butter.api.API;
import me.butter.api.player.PlayerHandler;

public final class Impl extends API {

    private static Impl instance;

    @Override
    public void onLoad() {
        API.setInstance(this);
        Impl.instance = this;
    }

    public static Impl get() {
        return instance;
    }

    @Override
    public PlayerHandler getPlayerHandler() {
        return null;
    }
}
