package me.butter.impl.player;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Menu;
import me.butter.api.module.roles.Role;
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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class UHCPlayerImpl implements UHCPlayer {

    private UUID playerUUID;
    private String playerName;
    private PlayerState playerState;

    private Role role;

    private boolean canPickItems, noFall;
    private int diamondMined;

    private List<UUID> killedPlayers;

    private Location deathLocation, spawnLocation;

    private List<ItemStack> inventory, armor, stash;

    private List<Potion> playerPotionEffects;
    private int maxHealth, speedEffect, strengthEffect, resiEffect;


    public UHCPlayerImpl(Player player) {
        this.playerUUID = player.getUniqueId();
        this.playerName = player.getName();
        this.playerState = PlayerState.IN_SPEC;

        this.canPickItems = true;
        this.noFall = false;

        this.diamondMined = 0;

        this.killedPlayers = new ArrayList<>();

        this.spawnLocation = player.getLocation();
        this.deathLocation = player.getLocation();

        this.armor = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.saveInventory();
        this.stash = new ArrayList<>();

        this.playerPotionEffects = new ArrayList<>();
        this.maxHealth = (int) player.getMaxHealth();
        setMaxHealth(20);
        this.speedEffect = 0; this.strengthEffect = 0; this.resiEffect = 0;
        updateEffects();
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
    public void openMenu(Menu menu, boolean isPrevMenu) {
        if(getPlayer() == null) return;
        UHCAPI.getInstance().getMenuHandler().openMenu(this, menu, isPrevMenu);
    }

    @Override
    public UUID getUniqueId() {
        return playerUUID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UHCPlayerImpl)) return false;
        UHCPlayerImpl uhcPlayer = (UHCPlayerImpl) object;
        return playerUUID.equals(uhcPlayer.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerUUID);
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
    public Role getRole() {
        return role;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public int getDiamondMined() {
        return diamondMined;
    }

    @Override
    public void setDiamondMined(int amount) {
        this.diamondMined = amount;
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
    public boolean hasNoFall() {
        return noFall;
    }

    @Override
    public void setNoFall(boolean hasNoFall) {
        this.noFall = hasNoFall;
    }

    @Override
    public void revive() {
        setCanPickItems(false);
        getPlayer().teleport(getDeathLocation());
        getPlayer().setGameMode(GameMode.SURVIVAL);
        setPlayerState(PlayerState.IN_GAME);
        loadInventory();

        for (Entity entity : getDeathLocation().getWorld().getNearbyEntities(getDeathLocation(), 5, 100, 5)) {
            if (entity instanceof Item) {
                entity.remove();
            }
        }

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> setCanPickItems(true), 10);
    }

    @Override
    public void clearPlayer() {
        clearEffects(); clearInventory(); clearStash();
        killedPlayers.clear();
        playerPotionEffects.clear();

        if(getPlayer() == null) return;
        Player player = getPlayer();
        player.setGameMode(GameMode.SURVIVAL);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setLevel(0);
        player.setExp(0);
    }

    @Override
    public boolean isNextTo(UHCPlayer player, int radius) {
        if(getPlayer() == null || player.getPlayer() == null) return false;
        return (getPlayerState() == PlayerState.IN_GAME && player.getPlayerState() == PlayerState.IN_GAME && getPlayer().getLocation().distance(player.getPlayer().getLocation()) <= radius);
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
        if(getPlayer() == null) return spawnLocation;
        return getPlayer().getLocation();
    }

    @Override
    public List<ItemStack> getInventory() {
        if(getPlayer() == null) return inventory;
        return Arrays.asList(getPlayer().getInventory().getContents());
    }

    @Override
    public void setInventory(List<ItemStack> inventory) {
        if(getPlayer() == null) return;

        if(inventory.isEmpty())  {
            getPlayer().getInventory().clear();
        }
        else {
            for(int i = 0; i < inventory.size(); i++) {
                ItemStack itemStack = inventory.get(i);
                if(itemStack == null || i >= 36) continue;
                getPlayer().getInventory().setItem(i,itemStack);
            }
        }

        this.inventory = inventory;
    }

    @Override
    public List<ItemStack> getArmor() {
        if(getPlayer() == null) return armor;
        return Arrays.asList(getPlayer().getInventory().getArmorContents());
    }

    @Override
    public void setArmor(List<ItemStack> armor) {
        if(getPlayer() == null) return;
        if(armor.isEmpty())  {
            getPlayer().getInventory().setArmorContents(null);
        }
        else {
            for(int i = 0; i < armor.size(); i++) {
                ItemStack itemStack = armor.get(i);
                if (itemStack != null && i < 4) {
                    switch (i) {
                        case 0:
                            getPlayer().getInventory().setHelmet(itemStack);
                            break;
                        case 1:
                            getPlayer().getInventory().setChestplate(itemStack);
                            break;
                        case 2:
                            getPlayer().getInventory().setLeggings(itemStack);
                            break;
                        case 3:
                            getPlayer().getInventory().setBoots(itemStack);
                            break;
                    }
                }
            }
        }

        this.armor = armor;
    }

    @Override
    public void saveInventory() {
        if(getPlayer() == null) return;
        inventory = Arrays.asList(getPlayer().getInventory().getContents());
        armor = Arrays.asList(getPlayer().getInventory().getArmorContents());
        Collections.reverse(armor);
    }

    @Override
    public void loadInventory() {
        if(getPlayer() == null) return;
        setInventory(inventory);
        setArmor(armor);
    }

    @Override
    public void clearInventory() {
        if(getPlayer() == null || getPlayer().getInventory() == null) return;
        getPlayer().getInventory().clear();
        getPlayer().getInventory().setArmorContents(null);
        saveInventory();
    }

    @Override
    public void giveItem(ItemStack itemToGive, boolean canGoToStash) {
        if(getPlayer() == null) return;
        if(getPlayer().getInventory().firstEmpty() > -1 || getPlayer().getInventory().contains(itemToGive)) {
            getPlayer().getInventory().addItem(itemToGive);
        }
        else if(canGoToStash) {
            addItemToStash(itemToGive);
        }
        else {
            getPlayer().getLocation().getWorld().dropItem(getPlayer().getLocation(), itemToGive);
        }
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        if(getPlayer() == null) return;
        getPlayer().getInventory().setItem(slot, item);
    }

    @Override
    public List<ItemStack> getStash() {
        return stash;
    }

    @Override
    public void setStash(List<ItemStack> stash) {
        this.stash = stash;
    }

    @Override
    public void clearStash() {
        stash = new ArrayList<>();
    }

    @Override
    public void addItemToStash(ItemStack item) {
        if(!stash.contains(item))
            stash.add(item);
        else {
            item.setAmount(item.getAmount() + stash.get(stash.indexOf(item)).getAmount());
            stash.set(stash.indexOf(item), item);
        }
    }

    @Override
    public void removeItemFromStash(ItemStack item) {
        stash.remove(item);
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
    public void removePotionEffect(PotionEffectType effect) {
        playerPotionEffects.removeIf(potion -> potion.getEffect() == effect);
        if (getPlayer() != null && getPlayer().hasPotionEffect(effect))
            getPlayer().removePotionEffect(effect);
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
    public int getMaxHealth() {
        if(getPlayer() == null) return maxHealth;
        return (int) getPlayer().getMaxHealth();
    }

    @Override
    public void setMaxHealth(int amount) {
        maxHealth = amount;
        if(getPlayer() != null) getPlayer().setMaxHealth(maxHealth);
    }

    @Override
    public void addMaxHealth(int amount) {
        maxHealth += amount;
        if(getPlayer() != null) getPlayer().setMaxHealth(maxHealth);
    }

    @Override
    public void removeMaxHealth(int amount) {
        maxHealth -= amount;
        if(maxHealth < 1) maxHealth = 1;
        if(getPlayer() != null) getPlayer().setMaxHealth(maxHealth);
    }

    @Override
    public void clearEffects() {
        playerPotionEffects = new ArrayList<>();
        speedEffect = 0; strengthEffect = 0; resiEffect = 0;

        for(PotionEffect effect : getPlayer().getActivePotionEffects()) {
            getPlayer().removePotionEffect(effect.getType());
        }
        getPlayer().setWalkSpeed(0.2f);
    }

    @Override
    public int getSpeed() {
        return speedEffect;
    }

    @Override @Deprecated
    public void setSpeed(int amount) {
        speedEffect = amount;
        updateEffects();
    }

    @Override
    public void addSpeed(int amount) {
        speedEffect += amount;
        updateEffects();
    }

    @Override
    public void removeSpeed(int amount) {
        speedEffect -= amount;
        updateEffects();
    }

    @Override
    public int getStrength() {
        return strengthEffect;
    }

    @Override @Deprecated
    public void setStrength(int amount) {
        strengthEffect = amount;
        updateEffects();
    }

    @Override
    public void addStrength(int amount) {
        strengthEffect += amount;
        updateEffects();
    }

    @Override
    public void removeStrength(int amount) {
        strengthEffect -= amount;
        updateEffects();
    }

    @Override
    public int getResi() {
        return resiEffect;
    }

    @Override
    public void setResi(int amount) {
        resiEffect = amount;
        updateEffects();
    }

    @Override
    public void addResi(int amount) {
        resiEffect += amount;
        updateEffects();
    }

    @Override
    public void removeResi(int amount) {
        resiEffect -= amount;
        updateEffects();
    }

    @Override
    public void updateEffects() {
        if(getPlayer() == null) return;

        int speedLevel = speedEffect / 20, speedRest = speedEffect % 20;
        getPlayer().setWalkSpeed(0.2f * (1 + speedRest / 100f));

        removePotionEffect(PotionEffectType.SPEED);
        removePotionEffect(PotionEffectType.SLOW);
        if(speedLevel > 0) {
            addPotionEffect(PotionEffectType.SPEED, -1, speedLevel);
        }
        else if (speedLevel < 0) {
            addPotionEffect(PotionEffectType.SLOW, -1, -speedLevel);
        }

        if(strengthEffect > 0 && !hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            addPotionEffect(PotionEffectType.INCREASE_DAMAGE, -1, 1);
        }
        else if(strengthEffect <= 0 && hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
            removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }

        if(resiEffect > 0 && !hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE, -1, 1);
        }
        else if(resiEffect <= 0 && hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
            removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
        }
    }
}
