package me.butter.impl.task;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.tab.list.MainTab;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TeleportingTask extends BukkitRunnable {

    private final List<UHCPlayer> players;

    private int playerTeleported;

    public TeleportingTask() {
        this.players = Lists.newArrayList(UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby());
        this.playerTeleported = 0;
        UHCAPI.getInstance().getGameHandler().setGameState(GameState.TELEPORTING);
        this.runTaskTimer(UHCAPI.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if (this.players.isEmpty()) {
            Location teleport = new Location(UHCAPI.getInstance().getWorldHandler().getWorld(), 0.0D, 100, 0.0D);
            teleport.getChunk().load();

            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInSpec()) {
                uhcPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                uhcPlayer.clearInventory();
                Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> uhcPlayer.getPlayer().teleport(teleport), 5);
            }

            this.cancel();
            Bukkit.getScheduler().runTaskLater(UHCImpl.getInstance(), StartTask::new,20);
            return;
        }

        UHCPlayer uhcPlayer = this.players.get(0);

        if (uhcPlayer.getPlayer() != null) {
            double alpha = 6.283185307179586D * (1 - (float) playerTeleported / UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size());
            double x = UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() / 1.5D * Math.cos(alpha);
            double z = UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() / 1.5D * Math.sin(alpha);
            Location randomLoc = new Location(UHCAPI.getInstance().getWorldHandler().getWorld(),
                    x,
                    UHCAPI.getInstance().getWorldHandler().getWorld().getHighestBlockYAt((int) x, (int) z) + 1,
                    z
            );
            uhcPlayer.setSpawnLocation(randomLoc);

            if(!uhcPlayer.getSpawnLocation().getChunk().isLoaded()) uhcPlayer.getSpawnLocation().getChunk().load();

            new BukkitRunnable() {

                @Override
                public void run() {
                    setPlayerInGame(uhcPlayer);
                }
            }.runTaskLater(UHCAPI.getInstance(), 5);
        }
        else {
            UHCAPI.getInstance().getPlayerHandler().removePlayer(uhcPlayer);
        }

        playerTeleported += 1;
        UHCAPI.getInstance().getPlayerHandler().getPlayers().forEach(uhcPlayer1 -> uhcPlayer1.sendActionBar(
                "Téléportation de " + uhcPlayer.getName() + " (" + playerTeleported + "/" + UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size() + ")"
        ));
        UHCAPI.getInstance().getPlayerHandler().getPlayers().forEach(uhcPlayer1 -> uhcPlayer1.getPlayer().playSound(uhcPlayer1.getLocation(), Sound.NOTE_STICKS, 6.0F, 1.0F));

        this.players.remove(0);
    }

    private void setPlayerInGame(UHCPlayer uhcPlayer) {
        uhcPlayer.clearEffects();
        uhcPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);

        uhcPlayer.addPotionEffect(PotionEffectType.BLINDNESS, -1, 10);
        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> uhcPlayer.removeSpeed(100), 20);

        uhcPlayer.clearInventory();
        uhcPlayer.clearStash();
        uhcPlayer.setArmor(UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingArmor());
        uhcPlayer.setInventory(UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingInventory());
        uhcPlayer.saveInventory();

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> uhcPlayer.getPlayer().teleport(uhcPlayer.getSpawnLocation()), 10);
    }
}
