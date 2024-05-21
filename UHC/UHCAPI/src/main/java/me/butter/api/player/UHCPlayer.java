package me.butter.api.player;

import me.butter.api.menu.Menu;
import me.butter.api.module.roles.Role;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public interface UHCPlayer {

    void sendMessage(String message); void sendMessage(BaseComponent component);
    void sendActionBar(String text); void sendTitle(String text, ChatColor color);

    void openMenu(Menu menu, boolean isPrevMenu);

    boolean equals(Object obj);

    UUID getUniqueId();
    Player getPlayer(); boolean isPlayer(Player player);
    String getName(); void setName(String newName);

    PlayerState getPlayerState(); void setPlayerState(PlayerState newState);

    Role getRole(); void setRole(Role role);

    int getDiamondMined(); void setDiamondMined(int amount);

    boolean canPickItems(); void setCanPickItems(boolean canPickItems);

    void clearPlayer();

    List<UUID> getKilledPlayers(); void setKilledPlayers(List<UUID> killedPlayers);
    void addKilledPlayer(UHCPlayer player); void removeKilledPlayer(UHCPlayer player);
    void addKilledPlayer(Player player); void removeKilledPlayer(Player player);

    Location getSpawnLocation(); void setSpawnLocation(Location location);
    Location getDeathLocation(); void setDeathLocation(Location location);
    Location getLocation();

    List<ItemStack> getInventory(); void setInventory(List<ItemStack> inventory);
    List<ItemStack> getArmor(); void setArmor(List<ItemStack> inventory);
    void saveInventory(); void loadInventory(); void clearInventory();
    void giveItem(ItemStack item, boolean canGoToStash); void setItem(int slot, ItemStack item);
    List<ItemStack> getStash(); void setStash(List<ItemStack> stash); void clearStash();
    void addItemToStash(ItemStack item); void removeItemFromStash(ItemStack item);

    List<Potion> getPotionEffects(); @Deprecated void setPotionEffects(List<Potion> potionEffects);
    void clearEffects();
    void addPotionEffect(PotionEffectType effect, int duration, int level);
    void removePotionEffect(PotionEffectType effect);
    boolean hasPotionEffect(PotionEffectType effect); Potion getPotion(PotionEffectType effect);

    int getSpeed(); @Deprecated void setSpeed(int amount); void addSpeed(int amount); void removeSpeed(int amount);
    int getStrength(); @Deprecated void setStrength(int amount); void addStrength(int amount); void removeStrength(int amount);
    int getResi(); @Deprecated void setResi(int amount); void addResi(int amount); void removeResi(int amount);
    void updateEffects();
}
