package me.butter.ninjago.roles;

import me.butter.ninjago.roles.list.maitres.Ash;
import me.butter.ninjago.roles.list.maitres.Griffin;
import me.butter.ninjago.roles.list.maitres.Invizable;
import me.butter.ninjago.roles.list.maitres.Karlof;
import me.butter.ninjago.roles.list.ninjas.*;
import me.butter.ninjago.roles.list.serpents.*;
import me.butter.ninjago.roles.list.solos.Garmadon;
import me.butter.ninjago.roles.list.solos.Morro;
import me.butter.ninjago.roles.list.solos.Skylor;
import org.bukkit.Material;

public enum RoleEnum {
    WU(Wu.class, CampEnum.NINJA, Material.STICK, "Wu"),
    LLOYD(Lloyd.class, CampEnum.NINJA, Material.GOLD_SWORD, "Lloyd"),
    KAI(Kai.class, CampEnum.NINJA, Material.FIREBALL, "Kai"),
    JAY(Jay.class, CampEnum.NINJA, Material.LAPIS_ORE, "Jay"),
    ZANE(Zane.class, CampEnum.NINJA, Material.ICE, "Zane"),
    COLE(Cole.class, CampEnum.NINJA, Material.BEDROCK, "Cole"),
    NYA(Nya.class, CampEnum.NINJA, Material.WATER_BUCKET, "Nya"),
    MISAKO(Misako.class, CampEnum.NINJA, Material.FEATHER, "Misako"),
    PIXAL(PIXAL.class, CampEnum.NINJA, Material.DIODE, "PIXAL"),
    MYSTAKE(Mystake.class, CampEnum.NINJA, Material.POTION, "Mitsake"),
    DARETH(Dareth.class, CampEnum.NINJA, Material.BOW, "Dareth"),
    APPRENTI(Apprenti.class, CampEnum.NINJA, Material.LEATHER_HELMET, "Apprenti"),
    ED(Ed.class, CampEnum.NINJA, Material.CLAY, "Ed"),
    FACTEUR(Facteur.class, CampEnum.NINJA, Material.WOOD_DOOR, "Facteur"),

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

    GRIFFIN(Griffin.class, CampEnum.MASTER, Material.SUGAR, "Turner"),
    INVIZABLE(Invizable.class, CampEnum.MASTER, Material.GLOWSTONE, "Invizable"),
    KARLOF(Karlof.class, CampEnum.MASTER, Material.IRON_CHESTPLATE, "Karlof"),
    ASH(Ash.class, CampEnum.MASTER, Material.FEATHER, "Ash"),

    MORRO(Morro.class, CampEnum.SOLO, Material.SLIME_BALL, "Morro"),
    SKYLOR(Skylor.class, CampEnum.SOLO, Material.FIREWORK, "Skylor"),
    GARMADON(Garmadon.class, CampEnum.SOLO, Material.OBSIDIAN, "Garmadon"),
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
