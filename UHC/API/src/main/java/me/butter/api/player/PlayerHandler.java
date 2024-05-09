package me.butter.api.player;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface PlayerHandler {

    List<UHCPlayer> getPlayers();

    UHCPlayer getUHCPlayer(UUID uuid); UHCPlayer getUHCPlayer(Player player);

    void addPlayer(UHCPlayer uhcPlayer); void removePlayer(UHCPlayer uhcPlayer);

    List<UHCPlayer> getPlayersInLobby();

    List<UHCPlayer> getPlayersInGame();

    List<UHCPlayer> getPlayersDead();

    List<UHCPlayer> getPlayersInSpec();

    List<UHCPlayer> getPlayersHavingRole();

    List<UHCPlayer> getPlayersNotHavingRole();

    void resetPlayer();
}