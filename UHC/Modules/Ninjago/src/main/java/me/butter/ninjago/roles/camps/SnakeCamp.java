package me.butter.ninjago.roles.camps;

import me.butter.api.module.camp.Camp;

public class SnakeCamp implements Camp {
    @Override
    public String getName() {
        return "Serpents";
    }

    @Override
    public String getPrefix() {
        return "§5";
    }

    @Override
    public boolean isSolo() {
        return false;
    }
}
