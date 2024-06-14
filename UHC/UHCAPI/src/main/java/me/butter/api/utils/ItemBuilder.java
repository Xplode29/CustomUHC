package me.butter.api.utils;


import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemBuilder {
    private final ItemStack is;

    public ItemBuilder(Material m) {
        this(m, 1);
    }


    public ItemBuilder(ItemStack is) {
        this.is = is;
    }


    public ItemBuilder(Material m, int amount) {
        this.is = new ItemStack(m, amount);
    }


    public ItemBuilder(Material m, int amount, byte durability) {
        this.is = new ItemStack(m, amount, durability);
    }

    public ItemBuilder clone() {
        return new ItemBuilder(this.is);
    }

    public ItemBuilder setDurability(short dur) {
        this.is.setDurability(dur);
        return this;
    }

    public ItemBuilder setDurability(int dur) {
        setDurability((short) dur);
        return this;
    }

    public ItemBuilder setTexture(String hash) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        propertyMap.put("textures", new Property("textures", hash));
        SkullMeta skullMeta = (SkullMeta) this.is.getItemMeta();
        Class<?> c_skullMeta = skullMeta.getClass();
        try {
            Field f_profile = c_skullMeta.getDeclaredField("profile");
            f_profile.setAccessible(true);
            f_profile.set(skullMeta, profile);
            f_profile.setAccessible(false);
            this.is.setItemMeta(skullMeta);
            return this;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();

            return this;
        }
    }

    public ItemBuilder setName(String name) {
        ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', "§r" + name));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setCoolName(String name) {
        ItemMeta im = this.is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name + " &7▪ &7Clic-droit"));
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder hideItemFlags() {
        ItemMeta im = this.is.getItemMeta();
        for (ItemFlag value : ItemFlag.values()) {
            im.addItemFlags(value);
        }
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder hideEnchants() {
        ItemMeta im = this.is.getItemMeta();
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        this.is.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment ench) {
        this.is.removeEnchantment(ench);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) this.is.getItemMeta();
            im.setOwner(owner);
            this.is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }

        return this;
    }

    public ItemBuilder addEnchant(Enchantment ench, int level) {
        ItemMeta im = this.is.getItemMeta();
        im.addEnchant(ench, level, true);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        this.is.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setUnbreakable() {
        ItemMeta meta = this.is.getItemMeta();
        meta.spigot().setUnbreakable(true);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        ItemMeta im = this.is.getItemMeta();
        List<String> list = new ArrayList<>();
        for (String s : lore) {
            list.add(ChatColor.translateAlternateColorCodes('&', "§r" + s));
        }
        im.setLore(list);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta im = this.is.getItemMeta();
        List<String> list = new ArrayList<>();
        for (String s : lore) {
            list.add(ChatColor.translateAlternateColorCodes('&', "§r" + s));
        }
        im.setLore(list);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(String line) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains("§r" + line)) return this;
        lore.remove("§r" + line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder removeLoreLine(int index) {
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size()) return this;
        lore.remove(index);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String line) {
        line = ChatColor.translateAlternateColorCodes('&', "§r" + line);
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore()) lore = new ArrayList<>(im.getLore());
        lore.add(line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder addLoreLine(String line, int pos) {
        line = ChatColor.translateAlternateColorCodes('&', line);
        ItemMeta im = this.is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, line);
        im.setLore(lore);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemBuilder setDyeColor(DyeColor color) {
        this.is.setDurability(color.getData());
        return this;
    }

    @Deprecated
    public ItemBuilder setWoolColor(DyeColor color) {
        if (!this.is.getType().equals(Material.WOOL)) return this;
        this.is.setDurability(color.getData());
        return this;
    }

    public ItemBuilder setLeatherArmorColor(Color color) {
        try {
            LeatherArmorMeta im = (LeatherArmorMeta) this.is.getItemMeta();
            im.setColor(color);
            this.is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }

        return this;
    }

    public ItemBuilder setBannerColor(DyeColor color) {
        try {
            BannerMeta im = (BannerMeta) this.is.getItemMeta();
            im.setBaseColor(color);
            this.is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }

        return this;
    }

    public ItemBuilder addBannerPattern(Pattern pattern) {
        try {
            BannerMeta im = (BannerMeta) this.is.getItemMeta();
            im.addPattern(pattern);
            this.is.setItemMeta(im);
        } catch (ClassCastException ignored) {
        }

        return this;
    }

    public ItemStack toItemStack() {
        return this.is;
    }
}