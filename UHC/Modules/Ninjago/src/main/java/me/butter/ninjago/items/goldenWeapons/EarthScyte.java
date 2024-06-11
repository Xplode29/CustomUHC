package me.butter.ninjago.items.goldenWeapons;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.roles.list.ninjas.Cole;
import me.butter.ninjago.roles.list.ninjas.Jay;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class EarthScyte extends AbstractGoldenWeapon {

    List<UHCPlayer> nearPlayers;

    public EarthScyte() {
        super("Faux de terre", Material.DIAMOND_HOE, 10 * 60);

        nearPlayers = new ArrayList<>();
    }

    @Override
    public boolean onEnable() {
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous utilisez la faux de terre !"));

        nearPlayers.clear();
        for (UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(getHolder().isNextTo(player, 20)) {
                nearPlayers.add(player);
                player.setAbleToMove(false);
            }
        }
        getHolder().setAbleToMove(false);

        new BukkitRunnable() {
            @Override
            public void run() {
                for (UHCPlayer player : nearPlayers) {
                    player.setAbleToMove(true);
                }
                getHolder().setAbleToMove(true);
            }
        }.runTaskLater(UHCAPI.getInstance(), 2 * 20);

        return true;
    }

    @Override
    public void onPickup() {
        getHolder().addResi(5);
        if(getHolder().getRole() instanceof Cole || getHolder().getRole() instanceof Lloyd) getHolder().addResi(5);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ramassé la faux de terre !"));
    }

    @Override
    public void onDrop() {
        getHolder().removeResi(5);
        if(getHolder().getRole() instanceof Cole || getHolder().getRole() instanceof Lloyd) getHolder().removeResi(5);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez laché la faux de terre !"));
    }
}
