package me.butter.impl.task;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.BlockUtils;
import me.butter.impl.UHCImpl;
import me.butter.impl.player.PotionUpdaterTask;
import me.butter.impl.scoreboard.list.GameScoreboard;
import me.butter.impl.tab.list.GameTab;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class StartTask extends BukkitRunnable {

    private int timer;

    public StartTask() {
        this.timer = 10;
        UHCAPI.get().getGameHandler().setGameState(GameState.STARTING);

        UHCAPI.get().getPlayerHandler().getPlayers().forEach(this::setPlayerInGame);

        this.runTaskTimer(UHCImpl.get(), 0, 20);
    }

    @Override
    public void run() {
        this.timer -= 1;

        for (UHCPlayer uhcPlayer : UHCAPI.get().getPlayerHandler().getPlayers()) {
            if(timer == 0) {
                uhcPlayer.sendTitle("Bonne Chance !", ChatColor.GREEN);
                uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_BASS, 6.0F, 1.0F);
            }
            else {
                uhcPlayer.sendTitle("" + timer, ChatColor.GREEN);
                uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.NOTE_PLING, 6.0F, 1.0F);
            }
        }

        if (timer == 0) {
            for(UHCPlayer uhcPlayer : UHCAPI.get().getPlayerHandler().getPlayersInLobby()) {
                uhcPlayer.setPlayerState(PlayerState.IN_GAME);
                int platformSize = 5;
                BlockUtils.fillBlocks(uhcPlayer.getSpawnLocation().getWorld(), uhcPlayer.getSpawnLocation().getBlockX() - platformSize/2, uhcPlayer.getSpawnLocation().getBlockY() - 1, uhcPlayer.getSpawnLocation().getBlockZ() - platformSize/2, platformSize, 1, platformSize, Material.AIR);

                UHCAPI.get().getTabHandler().setPlayerTab(GameTab.class, uhcPlayer);
                UHCAPI.get().getScoreboardHandler().setPlayerScoreboard(GameScoreboard.class, uhcPlayer);
            }

            UHCAPI.get().getGameHandler().setGameState(GameState.IN_GAME);
            this.cancel();

            new GameTask();
            new PotionUpdaterTask();
        }
    }

    private void setPlayerInGame(UHCPlayer uhcPlayer) {
        if (uhcPlayer.getPlayerState().equals(PlayerState.IN_LOBBY)) {
            uhcPlayer.clearInventory();

            if (!UHCAPI.get().getGameHandler().getItemConfig().getStartingInventory().isEmpty()) {
                for (ItemStack itemStack : UHCAPI.get().getGameHandler().getItemConfig().getStartingInventory()) {
                    if (itemStack == null) {
                        uhcPlayer.getPlayer().getInventory().addItem(new ItemStack(Material.AIR));
                    }
                    else {
                        uhcPlayer.getPlayer().getInventory().addItem(itemStack);
                    }
                }
            }
            if(!UHCAPI.get().getGameHandler().getItemConfig().getStartingArmor().isEmpty()) {
                for(int i = 0; i < UHCAPI.get().getGameHandler().getItemConfig().getStartingArmor().size(); i++) {
                    ItemStack itemStack = UHCAPI.get().getGameHandler().getItemConfig().getStartingArmor().get(i);
                    if (itemStack != null) {
                        switch (i) {
                            case 0:
                                uhcPlayer.getPlayer().getInventory().setHelmet(itemStack);
                                break;
                            case 1:
                                uhcPlayer.getPlayer().getInventory().setChestplate(itemStack);
                                break;
                            case 2:
                                uhcPlayer.getPlayer().getInventory().setLeggings(itemStack);
                                break;
                            case 3:
                                uhcPlayer.getPlayer().getInventory().setBoots(itemStack);
                                break;
                        }
                    }
                }
            }
            uhcPlayer.saveInventory();
        }
    }
}
