package me.butter.ninjago.roles;

import me.butter.api.module.camp.Camp;
import me.butter.ninjago.roles.camps.NinjaCamp;

public enum CampEnum {
    NINJA(new NinjaCamp());

    private Camp camp;
    CampEnum(Camp camp) {
        this.camp = camp;
    }

    public Camp getCamp() {
        return camp;
    }
}
