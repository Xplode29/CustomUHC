package me.butter.ninjago.roles;

import me.butter.ninjago.roles.list.ninjas.*;
import org.bukkit.Material;

public enum RoleEnum {
    LLOYD(Lloyd.class, CampEnum.NINJA, Material.GOLD_SWORD, "Lloyd"),
    KAI(Kai.class, CampEnum.NINJA, Material.FIREBALL, "Kai"),
    NYA(Nya.class, CampEnum.NINJA, Material.WATER_BUCKET, "Nya"),
    ZANE(Zane.class, CampEnum.NINJA, Material.ICE, "Zane"),
    COLE(Cole.class, CampEnum.NINJA, Material.BEDROCK, "Cole"),
    WU(Wu.class, CampEnum.NINJA, Material.STICK, "Wu"),
    JAY(Jay.class, CampEnum.NINJA, Material.LAPIS_ORE, "Jay"),
    PIXAL(PIXAL.class, CampEnum.NINJA, Material.DIODE, "PIXAL"),
    MITSAKE(Mitsake.class, CampEnum.NINJA, Material.POTION, "Mitsake"),
    DARETH(Dareth.class, CampEnum.NINJA, Material.BOW, "Dareth"),
    ED(Ed.class, CampEnum.NINJA, Material.CLAY, "Ed"),
    FACTEUR(Facteur.class, CampEnum.NINJA, Material.WOOD_DOOR, "Facteur"),
    APPRENTI(Apprenti.class, CampEnum.NINJA, Material.LEATHER_HELMET, "Apprenti"),
    MISAKO(Misako.class, CampEnum.NINJA, Material.FEATHER, "Misako");

    //PYTHOR(Pythor.class, CampEnum.SERPENT, Material.WATER_LILY),
    //SKALES(Skales.class, CampEnum.SERPENT, Material.BEACON),
    //ACIDICUS(Acidicus.class, CampEnum.SERPENT, Material.POISONOUS_POTATO),
    //SKALIDOR(Skalidor.class, CampEnum.SERPENT, Material.STONE),
    //FANGTOM(Fangtom.class, CampEnum.SERPENT, Material.TORCH),
    //FANGDAM(Fangdam.class, CampEnum.SERPENT, Material.REDSTONE_TORCH_ON),
    //BYTAR(Bytar.class, CampEnum.SERPENT, Material.DIRT),
    //LIZARU(Lizaru.class, CampEnum.SERPENT, Material.POTATO_ITEM),
    //SLITHRAA(Slithraa.class, CampEnum.SERPENT, Material.STAINED_GLASS),

    //VITESSE(Vitesse.class, CampEnum.MAITRES, Material.SUGAR),
    //LUMIERE(Lumiere.class, CampEnum.MAITRES, Material.GLOWSTONE),
    //METAL(Metal.class, CampEnum.MAITRES, Material.IRON_CHESTPLATE),
    //FUMEE(Fumee.class, CampEnum.MAITRES, Material.FEATHER),

    //MORRO(Morro.class, CampEnum.SOLO, Material.SLIME_BALL),
    //GARMADON(Garmadon.class, CampEnum.SOLO, Material.OBSIDIAN),
    //SKYLOR(Skylor.class, CampEnum.SOLO, Material.FIREWORK);

    private Class<? extends NinjagoRole> roleClass;
    private CampEnum camp;
    private Material icon;
    private String name;

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
