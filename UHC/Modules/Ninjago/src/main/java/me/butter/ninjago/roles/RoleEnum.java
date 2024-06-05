package me.butter.ninjago.roles;

import me.butter.ninjago.roles.list.maitres.*;
import me.butter.ninjago.roles.list.ninjas.*;
import me.butter.ninjago.roles.list.serpents.*;
import me.butter.ninjago.roles.list.solos.*;
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
    MISAKO(Misako.class, CampEnum.NINJA, Material.FEATHER, "Misako"),

    PYTHOR(Pythor.class, CampEnum.SNAKE, Material.WATER_LILY, "Pythor"),
    SKALES(Skales.class, CampEnum.SNAKE, Material.BEACON, "Skales"),
    ACIDICUS(Acidicus.class, CampEnum.SNAKE, Material.POISONOUS_POTATO, "Acidicus"),
    SKALIDOR(Skalidor.class, CampEnum.SNAKE, Material.STONE, "Skalidor"),
    FANGDAM(Fangdam.class, CampEnum.SNAKE, Material.REDSTONE_TORCH_ON, "Fangdam"),
    FANGTOM(Fangtom.class, CampEnum.SNAKE, Material.TORCH, "Fangtom"),
    BYTAR(Bytar.class, CampEnum.SNAKE, Material.DIRT, "Bytar"),
    LIZARU(Lizaru.class, CampEnum.SNAKE, Material.POTATO_ITEM, "Lizaru"),
    SLITHRAA(Slithraa.class, CampEnum.SNAKE, Material.STAINED_GLASS, "Slithraa"),
    ARCTURUS(Arcturus.class, CampEnum.SNAKE, Material.DARK_OAK_DOOR_ITEM, "Arcturus"),

    VITESSE(Vitesse.class, CampEnum.MASTER, Material.SUGAR, "Griffin Turner (Maitre de la vitesse)"),
    LUMIERE(Lumiere.class, CampEnum.MASTER, Material.GLOWSTONE, "Invizable (Maitre de la lumière)"),
    METAL(Metal.class, CampEnum.MASTER, Material.IRON_CHESTPLATE, "Karlof (Maitre du métal)"),
    FUMEE(Fumee.class, CampEnum.MASTER, Material.FEATHER, "Ash (Maitre de la fumée)"),

    MORRO(Morro.class, CampEnum.SOLO, Material.SLIME_BALL, "Morro"),
    GARMADON(Garmadon.class, CampEnum.SOLO, Material.OBSIDIAN, "Garmadon"),
    SKYLOR(Skylor.class, CampEnum.SOLO, Material.FIREWORK, "Skylor"),
    ;

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
