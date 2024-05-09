package me.butter.api.player;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface UHCPlayer {

    void sendMessage(String message); void sendMessage(BaseComponent component);

    void sendActionBar(String text);

    void sendTitle(String title, String subtitle);

    UUID getUUID();

    Player getPlayer(); boolean isPlayer(Player player);

    String getName(); void setName(String newName);

    PlayerState getPlayerState(); void setPlayerState();

    Location getLocation(); void setLocation();

    List<Potion> getPotionEffects(); void setPotionEffects(List<Potion> potionEffects);
    void addPotionEffect(Potion potion); void removePotionEffect(Potion potion);

    int getSpeed(); @Deprecated void setSpeed(int amount); void addSpeed(int amount); void removeSpeed(int amount);

    int getStrength(); @Deprecated void setStrength(int amount); void addStrength(int amount); void removeStrength(int amount);

    int getResi(); @Deprecated void setResi(int amount); void addResi(int amount); void removeResi(int amount);
}
