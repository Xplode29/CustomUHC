package me.butter.ninjago.roles;

import me.butter.api.module.camp.Camp;
import me.butter.ninjago.roles.camps.*;
import org.bukkit.Material;

public enum CampEnum {
    NINJA(new NinjaCamp(), Material.IRON_INGOT),
    SNAKE(new SnakeCamp(), Material.GOLD_INGOT),
    MASTER(new MaitreCamp(), Material.EMERALD),
    SOLO(new SoloCamp(), Material.DIAMOND),

    DUO_LLOYD_GARMADON(new LloydGarmadonCamp(), Material.EMERALD, true);

    private final Camp camp;
    private final Material icon;
    private final boolean hidden;

    CampEnum(Camp camp, Material icon) {
        this.camp = camp;
        this.icon = icon;
        this.hidden = false;
    }

    CampEnum(Camp camp, Material icon, boolean hidden) {
        this.camp = camp;
        this.icon = icon;
        this.hidden = hidden;
    }

    public Camp getCamp() {
        return camp;
    }

    public Material getIcon() {
        return icon;
    }

    public boolean isHidden() {
        return hidden;
    }
}
