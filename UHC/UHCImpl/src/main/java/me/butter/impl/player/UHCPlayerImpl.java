package me.butter.impl.player;

import me.butter.api.player.PlayerState;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UHCPlayerImpl implements UHCPlayer {

    UUID playerUUID;
    String playerName;
    PlayerState playerState;

    boolean canPickItems;

    List<UUID> killedPlayers;

    Location playerLocation, deathLocation, spawnLocation;

    List<ItemStack> inventory;

    List<Potion> playerPotionEffects;
    int speedEffect, strengthEffect, resiEffect;


    public UHCPlayerImpl(Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerState = PlayerState.IN_SPEC;

        this.canPickItems = true;

        this.killedPlayers = new ArrayList<>();

        this.playerLocation = player.getLocation();
        this.spawnLocation = player.getLocation();
        this.deathLocation = player.getLocation();

        this.inventory = new ArrayList<>();
        this.saveInventory();

        this.playerPotionEffects = new ArrayList<>();
        this.speedEffect = 0; this.strengthEffect = 0; this.resiEffect = 0;
    }

    @Override
    public void sendMessage(String message) {
        if(getPlayer() == null) return;
        getPlayer().sendMessage(message);
    }

    @Override
    public void sendMessage(BaseComponent component) {
        if(getPlayer() == null) return;
        getPlayer().spigot().sendMessage(component);
    }

    @Override
    public void sendActionBar(String text) {
        if(getPlayer() == null) return;
        PacketPlayOutChat packet = new PacketPlayOutChat(new ChatComponentText(text), (byte)2);
        ((CraftPlayer) getPlayer()).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void sendTitle(String text, ChatColor color) {
        if(getPlayer() == null) return;
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\",color:" + color.name().toLowerCase() + "}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);


        ((CraftPlayer) getPlayer()).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) getPlayer()).getHandle().playerConnection.sendPacket(length);
    }

    @Override
    public UUID getUniqueId() {
        return playerUUID;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(playerUUID);
    }

    @Override
    public boolean isPlayer(Player player) {
        return (player.getUniqueId().equals(playerUUID));
    }

    @Override
    public String getName() {
        return playerName;
    }

    @Override
    public void setName(String newName) {
        playerName = newName;
    }

    @Override
    public PlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public void setPlayerState(PlayerState newState) {
        playerState = newState;
    }

    @Override
    public boolean canPickItems() {
        return canPickItems;
    }

    @Override
    public void setCanPickItems(boolean canPickItems) {
        this.canPickItems = canPickItems;
    }

    @Override
    public List<UUID> getKilledPlayers() {
        return killedPlayers;
    }

    @Override
    public void setKilledPlayers(List<UUID> killedPlayers) {
        this.killedPlayers = killedPlayers;
    }

    @Override
    public void addKilledPlayer(UHCPlayer player) {
        killedPlayers.add(player.getUniqueId());
    }
    @Override
    public void addKilledPlayer(Player player) {
        killedPlayers.add(player.getUniqueId());
    }

    @Override
    public void removeKilledPlayer(UHCPlayer player) {
        killedPlayers.remove(player.getUniqueId());
    }
    @Override
    public void removeKilledPlayer(Player player) {
        killedPlayers.remove(player.getUniqueId());
    }

    @Override
    public Location getLocation() {
        return playerLocation;
    }

    @Override
    public void setLocation(Location newLocation) {
        playerLocation = newLocation.clone();
    }

    @Override
    public List<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public void saveInventory() {
        if(getPlayer() == null) return;
        inventory.clear();
        for(int i = 0; i < 40; i++) {
            ItemStack itemStack = getPlayer().getInventory().getItem(i);
            if(itemStack == null) itemStack = new ItemStack(Material.AIR);
            inventory.add(itemStack);
        }
    }

    @Override
    public void loadInventory() {
        if(getPlayer() == null) return;
        for(int i = 0; i < 40; i++) {
            ItemStack itemStack = inventory.get(i);
            if(itemStack == null) itemStack = new ItemStack(Material.AIR);
            getPlayer().getInventory().setItem(i, itemStack);
        }
    }

    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public void setSpawnLocation(Location location) {
        spawnLocation = location.clone();
    }

    @Override
    public Location getDeathLocation() {
        return deathLocation;
    }

    @Override
    public void setDeathLocation(Location location) {
        deathLocation = location.clone();
    }

    @Override
    public List<Potion> getPotionEffects() {
        return playerPotionEffects;
    }

    @Override @Deprecated
    public void setPotionEffects(List<Potion> potionEffects) {
        playerPotionEffects = potionEffects;
    }

    @Override
    public void addPotionEffect(PotionEffectType effect, int duration, int level) {
        for(Potion potion : playerPotionEffects) {
            if(potion.getEffect() == effect && potion.getLevel() == level) {
                potion.setDuration(duration);
                return;
            }
        }
        playerPotionEffects.add(new PotionImpl(effect, duration, level));
    }

    @Override
    public void removePotionEffect(PotionEffectType effect, int level) {
        playerPotionEffects.removeIf(potion -> potion.getEffect() == effect && potion.getLevel() == level);
    }

    @Override
    public boolean hasPotionEffect(PotionEffectType effect) {
        for(Potion potion : playerPotionEffects) {
            if(potion.getEffect() == effect) return true;
        }
        return false;
    }

    @Override
    public Potion getPotion(PotionEffectType effect) {
        for(Potion potion : playerPotionEffects) {
            if(potion.getEffect() == effect) return potion;
        }
        return null;
    }

    @Override
    public void clearEffects() {
        playerPotionEffects = new ArrayList<>();
        speedEffect = 0; strengthEffect = 0; resiEffect = 0;
    }

    @Override
    public int getSpeed() {
        return speedEffect;
    }

    @Override @Deprecated
    public void setSpeed(int amount) {
        speedEffect = amount;
    }

    @Override
    public void addSpeed(int amount) {
        speedEffect += amount;
    }

    @Override
    public void removeSpeed(int amount) {
        speedEffect -= amount;
    }

    @Override
    public int getStrength() {
        return strengthEffect;
    }

    @Override @Deprecated
    public void setStrength(int amount) {
        strengthEffect = amount;
    }

    @Override
    public void addStrength(int amount) {
        strengthEffect += amount;
    }

    @Override
    public void removeStrength(int amount) {
        strengthEffect -= amount;
    }

    @Override
    public int getResi() {
        return resiEffect;
    }

    @Override
    public void setResi(int amount) {
        resiEffect = amount;
    }

    @Override
    public void addResi(int amount) {
        resiEffect += amount;
    }

    @Override
    public void removeResi(int amount) {
        resiEffect -= amount;
    }
}
