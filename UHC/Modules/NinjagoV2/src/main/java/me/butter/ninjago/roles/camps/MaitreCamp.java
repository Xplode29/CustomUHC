package me.butter.ninjago.roles.camps;

import me.butter.api.module.camp.Camp;

public class MaitreCamp implements Camp {
    @Override
    public String getName() {
        return "Alliance des elements";
    }

    @Override
    public String getPrefix() {
        return "§b";
    }

    @Override
    public boolean isSolo() {
        return false;
    }
}
