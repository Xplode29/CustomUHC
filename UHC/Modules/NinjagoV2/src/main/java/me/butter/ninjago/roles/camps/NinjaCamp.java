package me.butter.ninjago.roles.camps;

import me.butter.api.module.camp.Camp;

public class NinjaCamp implements Camp {
    @Override
    public String getName() {
        return "Ninjas";
    }

    @Override
    public String getPrefix() {
        return "§a";
    }

    @Override
    public boolean isSolo() {
        return false;
    }
}
