package me.butter.ninjago.roles;

import me.butter.api.module.camp.Camp;
import me.butter.ninjago.roles.camps.MaitreCamp;
import me.butter.ninjago.roles.camps.NinjaCamp;
import me.butter.ninjago.roles.camps.SnakeCamp;
import org.bukkit.Material;

public enum CampEnum {
    NINJA(new NinjaCamp(), Material.DIAMOND),
    SNAKE(new SnakeCamp(), Material.GOLD_INGOT),
    MASTER(new MaitreCamp(), Material.EMERALD);

    private Camp camp;
    private Material icon;

    CampEnum(Camp camp, Material icon) {
        this.camp = camp;
        this.icon = icon;
    }

    public Camp getCamp() {
        return camp;
    }

    public Material getIcon() {
        return icon;
    }
}
