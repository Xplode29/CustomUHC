package me.butter.ninjago.roles;

import me.butter.ninjago.roles.list.ninjas.*;
import org.bukkit.Material;

public enum RoleEnum {
    KAI(Kai.class, CampEnum.NINJA, Material.FIREBALL, "Kai"),
    JAY(Jay.class, CampEnum.NINJA, Material.LAPIS_ORE, "Jay"),
    COLE(Cole.class, CampEnum.NINJA, Material.BEDROCK, "Cole"),
    ;

    private final Class<? extends NinjagoRole> roleClass;
    private final CampEnum camp;
    private final Material icon;
    private final String name;

    private int amount;

    RoleEnum(Class<? extends NinjagoRole> roleClass, CampEnum camp, Material icon, String name) {
        this.name = name;
        this.icon = icon;
        this.roleClass = roleClass;
        this.camp = camp;
        this.amount = 0;
    }

    public Class<? extends NinjagoRole> getRoleClass() {
        return roleClass;
    }

    public CampEnum getCampEnum() {
        return camp;
    }

    public Material getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
