package me.butter.ninjago.roles.camps;

import me.butter.api.module.camp.Camp;

public class SoloCamp implements Camp {
    @Override
    public String getName() {
        return "Solitaires";
    }

    @Override
    public boolean isSolo() {
        return true;
    }
}