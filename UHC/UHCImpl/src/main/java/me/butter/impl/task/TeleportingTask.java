package me.butter.impl.task;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.UHCImpl;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.tab.list.GameTab;
import org.bukkit.Bukkit;
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
        this.runTaskTimer(UHCAPI.getInstance(), 0, 5);
    }

    @Override
    public void run() {
        if (this.players.isEmpty()) {
            this.cancel();
            Bukkit.getScheduler().runTaskLater(UHCImpl.getInstance(), StartTask::new,20 * 5);
            return;
        }

        UHCPlayer uhcPlayer = this.players.get(0);

        if (uhcPlayer.getPlayer() != null) {
            if(!uhcPlayer.getSpawnLocation().getChunk().isLoaded()) uhcPlayer.getSpawnLocation().getChunk().load();

            new BukkitRunnable() {

                @Override
                public void run() {
                    setPlayerInGame(uhcPlayer);
                }
            }.runTaskLater(UHCAPI.getInstance(), 5);
        }

        playerTeleported += 1;
        UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().forEach(uhcPlayer1 -> uhcPlayer1.sendActionBar(
                "Téléportation de " + uhcPlayer.getName() + " (" + playerTeleported + "/" + UHCAPI.getInstance().getPlayerHandler().getPlayersInLobby().size() + ")"
        ));

        uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_STICKS, 6.0F, 1.0F);

        this.players.remove(0);
    }

    private void setPlayerInGame(UHCPlayer uhcPlayer) {
        UHCAPI.getInstance().getTabHandler().setPlayerTab(GameTab.class, uhcPlayer);
        UHCAPI.getInstance().getScoreboardHandler().setPlayerScoreboard(GameScoreboard.class, uhcPlayer);

        uhcPlayer.setPlayerState(PlayerState.IN_GAME);

        if (uhcPlayer.getPlayer() == null) return;

        uhcPlayer.clearEffects();
        uhcPlayer.addPotionEffect(PotionEffectType.BLINDNESS, -1, 10);
        uhcPlayer.addPotionEffect(PotionEffectType.SLOW, -1, 10);

        uhcPlayer.clearInventory();
        uhcPlayer.clearStash();
        uhcPlayer.setArmor(UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingArmor());
        uhcPlayer.setInventory(UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingInventory());
        uhcPlayer.saveInventory();

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> uhcPlayer.getPlayer().teleport(uhcPlayer.getSpawnLocation()), 10);
    }
}
