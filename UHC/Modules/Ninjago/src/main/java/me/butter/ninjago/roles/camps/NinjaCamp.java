package me.butter.ninjago.roles.camps;

import me.butter.api.UHCAPI;
import me.butter.api.module.camp.Camp;
import me.butter.api.module.roles.Role;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.RoleEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NinjaCamp implements Camp {
    @Override
    public String getName() {
        return "Ninjas";
    }
}
