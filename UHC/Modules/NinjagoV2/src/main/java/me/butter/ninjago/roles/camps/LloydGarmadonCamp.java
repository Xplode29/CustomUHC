package me.butter.ninjago.roles.camps;

import me.butter.api.module.camp.Camp;

public class LloydGarmadonCamp implements Camp {
    @Override
    public String getName() {
        return "Duo Lloyd/Garmadon";
    }

    @Override
    public String getPrefix() {
        return "§e";
    }

    @Override
    public boolean isSolo() {
        return false;
    }
}