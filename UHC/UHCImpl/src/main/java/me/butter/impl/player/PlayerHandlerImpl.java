package me.butter.impl.player;

import me.butter.api.player.PlayerHandler;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerHandlerImpl implements PlayerHandler {

    private List<UHCPlayer> uhcPlayers;

    public PlayerHandlerImpl() {
        uhcPlayers = new ArrayList<>();
    }

    @Override
    public List<UHCPlayer> getPlayers() {
        return uhcPlayers;
    }

    @Override
    public UHCPlayer getUHCPlayer(String name) {
        for(UHCPlayer uhcPlayer : uhcPlayers) {
            if(uhcPlayer.getName().equalsIgnoreCase(name)) {
                return uhcPlayer;
            }
        }
        return null;
    }

    @Override
    public UHCPlayer getUHCPlayer(UUID uuid) {
        for(UHCPlayer uhcPlayer : uhcPlayers) {
            if(uhcPlayer.getUniqueId().equals(uuid)) {
                return uhcPlayer;
            }
        }
        return null;
    }

    @Override
    public UHCPlayer getUHCPlayer(Player player) {
        for(UHCPlayer uhcPlayer : uhcPlayers) {
            if(uhcPlayer.getUniqueId().equals(player.getUniqueId())) {
                return uhcPlayer;
            }
        }
        return null;
    }

    @Override
    public void addPlayer(Player player) {
        UHCPlayer uhcPlayer = new UHCPlayerImpl(player);
        uhcPlayers.add(uhcPlayer);
    }

    @Override
    public void removePlayer(Player player) {
        UHCPlayer uhcPlayer = getUHCPlayer(player.getUniqueId());
        uhcPlayers.remove(uhcPlayer);
    }

    @Override
    public List<UHCPlayer> getPlayersInLobby() {
        List<UHCPlayer> inLobby = new ArrayList<>();
        for(UHCPlayer uhcPlayer : uhcPlayers) {
            if(uhcPlayer.getPlayerState() == PlayerState.IN_LOBBY) {
                inLobby.add(uhcPlayer);
            }
        }
        return inLobby;
    }

    @Override
    public List<UHCPlayer> getPlayersInGame() {
        List<UHCPlayer> inGame = new ArrayList<>();
        for(UHCPlayer uhcPlayer : uhcPlayers) {
            if(uhcPlayer.getPlayerState() == PlayerState.IN_GAME) {
                inGame.add(uhcPlayer);
            }
        }
        return inGame;
    }

    @Override
    public List<UHCPlayer> getPlayersDead() {
        List<UHCPlayer> deadPlayers = new ArrayList<>();
        for(UHCPlayer uhcPlayer : uhcPlayers) {
            if(uhcPlayer.getPlayerState() == PlayerState.DEAD) {
                deadPlayers.add(uhcPlayer);
            }
        }
        return deadPlayers;
    }

    @Override
    public List<UHCPlayer> getPlayersInSpec() {
        List<UHCPlayer> inSpec = new ArrayList<>();
        for(UHCPlayer uhcPlayer : uhcPlayers) {
            if(uhcPlayer.getPlayerState() == PlayerState.IN_SPEC) {
                inSpec.add(uhcPlayer);
            }
        }
        return inSpec;
    }
}
