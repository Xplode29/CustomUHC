package me.butter.impl.item.list;

import me.butter.impl.item.AbstractItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.util.Vector;

public class GrapplingItem extends AbstractItem implements Listener {
    public GrapplingItem() {
        super(Material.FISHING_ROD, "Grappling Hook");
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        Player player = event.getPlayer();

        if(event.getState().equals(PlayerFishEvent.State.FAILED_ATTEMPT) && isItemStack(player.getItemInHand())) {
            Location playerLocation = player.getLocation().clone();
            Location fishingLocation = event.getHook().getLocation().clone();
            Vector change = fishingLocation.toVector().subtract(playerLocation.toVector());

            player.setVelocity(change.multiply(0.3));
        }
    }
}
