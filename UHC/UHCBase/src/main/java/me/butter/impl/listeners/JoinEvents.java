package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleEffects;
import me.butter.api.utils.WorldUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.scoreboard.list.LobbyScoreboard;
import me.butter.impl.tab.list.MainTab;
import net.minecraft.server.v1_8_R3.MobEffect;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEffect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class JoinEvents implements Listener {

    private boolean generate;
    public static Location spawnLocation;

    public JoinEvents() {
        this.generate = false;
        spawnLocation = new Location(Bukkit.getWorld("world"), 0.0D, 201, 0.0D);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        if (!generate) {
            //Build the spawn
            int spawnSize = 40, spawnHeight = 10;
            List<Block> cube = WorldUtils.getCube(spawnLocation.clone().subtract((double) spawnSize / 2, 0, (double) spawnSize / 2), spawnSize, spawnHeight, spawnSize, true);

            for(Block block : cube) {
                block.setType(Material.AIR);
            }

            generate = true;
            spawnLocation.getChunk().load();
        }

        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player.getUniqueId());
        if (uhcPlayer == null) {
            UHCAPI.getInstance().getPlayerHandler().addPlayer(player);
            uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        }
        uhcPlayer.setName(player.getName());

        uhcPlayer.setDisconnected(false);
        uhcPlayer.setDisconnectionTime(0);

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY) {
            uhcPlayer.resetPlayer();
            uhcPlayer.setPlayerState(PlayerState.IN_LOBBY);

            UHCAPI.getInstance().getItemHandler().giveLobbyItems(uhcPlayer);

            UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(LobbyScoreboard.class, uhcPlayer);
            UHCAPI.getInstance().getTabHandler().setPlayerTab(MainTab.class, uhcPlayer);
            Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.teleport(spawnLocation), 2);

            ParticleEffects.tornadoEffect(uhcPlayer.getPlayer(), Color.fromRGB(255, 255, 255));

            uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Bienvenue en Ninjago UHC !"));
            uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Cet UHC est encore est beta, donc envoie les bugs sur le discord !"));
            uhcPlayer.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Pour plus d'informations sur le mode de jeu -> /doc"));
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.TELEPORTING) {
            Location teleport;

            if(uhcPlayer.getPlayerState() == PlayerState.IN_GAME) {
                teleport = uhcPlayer.getSpawnLocation();
                player.setGameMode(GameMode.SURVIVAL);
            }
            else {
                teleport = new Location(UHCAPI.getInstance().getWorldHandler().getWorld(), 0.0D, 100, 0.0D);
                uhcPlayer.clearInventory();
                player.setGameMode(GameMode.SPECTATOR);
            }

            teleport.getChunk().load();
            Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.teleport(teleport), 2);

            UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(GameScoreboard.class, uhcPlayer);
            UHCAPI.getInstance().getTabHandler().setPlayerTab(MainTab.class, uhcPlayer);
        }
        else if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.STARTING ||
                UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(uhcPlayer.getPlayerState() != PlayerState.DEAD && uhcPlayer.getPlayerState() != PlayerState.IN_GAME) {
                Location teleport = new Location(UHCAPI.getInstance().getWorldHandler().getWorld(), 0.0D, 100, 0.0D);

                player.setGameMode(GameMode.SPECTATOR);
                uhcPlayer.clearInventory();

                teleport.getChunk().load();
                Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.teleport(teleport), 2);
            }

            UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(GameScoreboard.class, uhcPlayer);
            UHCAPI.getInstance().getTabHandler().setPlayerTab(MainTab.class, uhcPlayer);
        }

        for(Potion potion : uhcPlayer.getPotionEffects()) {
            if(potion.isPacket()) {
                PacketPlayOutEntityEffect packet = new PacketPlayOutEntityEffect(
                        uhcPlayer.getPlayer().getEntityId(),
                        new MobEffect(potion.getEffect().getId(), (potion.getDuration() == -1 ? Integer.MAX_VALUE : potion.getDuration() * 20), potion.getLevel() - 1)
                );
                ((CraftPlayer) uhcPlayer.getPlayer()).getHandle().playerConnection.sendPacket(packet);
            }
        }
        uhcPlayer.addSpeed(0);

        event.setJoinMessage(null);
        Bukkit.broadcastMessage(ChatUtils.JOINED.getMessage(
                player.getDisplayName() + " [" +
                (UHCAPI.getInstance().getPlayerHandler().getPlayers().size() > UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                UHCAPI.getInstance().getPlayerHandler().getPlayers().size() + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.DARK_GRAY + "] "
        ));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if (uhcPlayer == null) return;

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.LOBBY) {
            UHCAPI.getInstance().getItemHandler().removeLobbyItems(uhcPlayer);
        }

        UHCAPI.getInstance().getScoreboardHandler().removePlayerScoreboard(uhcPlayer);
        UHCAPI.getInstance().getTabHandler().removePlayerTab(uhcPlayer);

        uhcPlayer.setDisconnected(true);
        uhcPlayer.setDeathLocation(player.getLocation());

        event.setQuitMessage(null);
        Bukkit.broadcastMessage(ChatUtils.LEFT.getMessage(
                player.getDisplayName() + " [" +
                        ((UHCAPI.getInstance().getPlayerHandler().getPlayers().size() - 1) > UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() ? ChatColor.RED : ChatColor.GREEN) +
                        (UHCAPI.getInstance().getPlayerHandler().getPlayers().size() - 1) + "/" + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.DARK_GRAY + "] "
        ));

        if(uhcPlayer.getPlayerState() == PlayerState.IN_LOBBY || uhcPlayer.getPlayerState() == PlayerState.IN_SPEC) UHCAPI.getInstance().getPlayerHandler().removePlayer(uhcPlayer);
    }
}
