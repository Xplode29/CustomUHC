package me.butter.impl.player;

import me.butter.api.player.PlayerState;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UHCPlayerImpl implements UHCPlayer {

    UUID playerUUID;
    String playerName;
    PlayerState playerState;
    Location playerLocation;
    List<Potion> playerPotionEffects;

    List<String> messagesToSend;
    List<BaseComponent> componentsToSend;
    int speedEffect, strengthEffect, resiEffect;

    public UHCPlayerImpl(Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerState = PlayerState.IN_LOBBY;
        this.playerLocation = player.getLocation();
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
    public void sendTitle(String title, String subtitle) {
        if(getPlayer() == null) return;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public boolean isPlayer(Player player) {
        return false;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void setName(String newName) {

    }

    @Override
    public PlayerState getPlayerState() {
        return null;
    }

    @Override
    public void setPlayerState() {

    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void setLocation() {

    }

    @Override
    public List<Potion> getPotionEffects() {
        return Collections.emptyList();
    }

    @Override
    public void setPotionEffects(List<Potion> potionEffects) {

    }

    @Override
    public void addPotionEffect(Potion potion) {

    }

    @Override
    public void removePotionEffect(Potion potion) {

    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public void setSpeed(int amount) {

    }

    @Override
    public void addSpeed(int amount) {

    }

    @Override
    public void removeSpeed(int amount) {

    }

    @Override
    public int getStrength() {
        return 0;
    }

    @Override
    public void setStrength(int amount) {

    }

    @Override
    public void addStrength(int amount) {

    }

    @Override
    public void removeStrength(int amount) {

    }

    @Override
    public int getResi() {
        return 0;
    }

    @Override
    public void setResi(int amount) {

    }

    @Override
    public void addResi(int amount) {

    }

    @Override
    public void removeResi(int amount) {

    }
}
