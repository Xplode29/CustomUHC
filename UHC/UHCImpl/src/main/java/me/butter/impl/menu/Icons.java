package me.butter.impl.menu;

import me.butter.api.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

public enum Icons {
    MINUS_BIG("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGU0YjhiOGQyMzYyYzg2NGUwNjIzMDE0ODdkOTRkMzI3MmE2YjU3MGFmYmY4MGMyYzViMTQ4Yzk1NDU3OWQ0NiJ9fX0="),
    MINUS_MEDIUM("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWNmZjkxZGM5OWQ1ODI4MDIzZWVkZjg3Mzc5OWQyNTUzNWRhZGU2NGEyZTE2YTNiNDk4YjQxMTNlYWZkNDk2NiJ9fX0="),
    MINUS_SMALL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQyNGRmYWYxZWUxN2Q3M2VlZWMyNDIyMTU4Y2EzM2FkMTg3ZWU3MjdhYmI3OTZmMjEzMmRlZGZkMDFmYzQ5ZSJ9fX0="),

    PLUS_BIG("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWFlYTVkNzEyNzIxNGVlMmNjYTFiODJlZjgyZjhhZTU1OTI5MzI1NzY2ZWE4NGRkZTE4YzExZGUwYzdkNTkxIn19fQ=="),
    PLUS_MEDIUM("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ4NmRiOWExNGQ1ODc5ZmEyODExZDMwMWNjYmQ1MjY5OTRmODcxMjQ3YjYyZjJkOWE0ODE4M2U5NjQxYWQ2OSJ9fX0="),
    PLUS_SMALL("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19");

    private final String base;


    Icons(String base) {
        this.base = base;
    }

    public String getBase() {
        return this.base;
    }

    public ItemStack toItemStack(String name) {
        return (new ItemBuilder(Material.SKULL_ITEM, 1, (byte) SkullType.PLAYER.ordinal()))
                .setTexture(getBase())
                .setName("§r" + name)
                .toItemStack();
    }
}