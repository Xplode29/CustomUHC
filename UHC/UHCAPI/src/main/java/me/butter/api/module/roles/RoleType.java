package me.butter.api.module.roles;

import me.butter.api.module.camp.Camp;
import org.bukkit.Material;

public interface RoleType {

    Class<? extends Role> getRoleClass();
    Camp getCamp();

    String getName();
    Material getIcon();
    int getAmount();
}
