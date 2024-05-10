package me.butter.api.player;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerHandler {

    List<UHCPlayer> getPlayers();

    UHCPlayer getUHCPlayer(UUID uuid); UHCPlayer getUHCPlayer(Player player);

    void addPlayer(Player player); void removePlayer(Player player);

    List<UHCPlayer> getPlayersInLobby();

    List<UHCPlayer> getPlayersInGame();

    List<UHCPlayer> getPlayersDead();

    List<UHCPlayer> getPlayersInSpec();
}