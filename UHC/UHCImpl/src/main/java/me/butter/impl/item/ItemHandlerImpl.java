package me.butter.impl.item;

import me.butter.api.UHCAPI;
import me.butter.api.item.CustomItem;
import me.butter.api.item.ItemHandler;
import me.butter.api.player.UHCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemHandlerImpl implements ItemHandler, Listener {

    List<CustomItem> customItems;

    public ItemHandlerImpl() {
        customItems = new ArrayList<>();

        //Register
        UHCAPI.get().getServer().getPluginManager().registerEvents(this, UHCAPI.get());
    }

    @Override
    public CustomItem getCustomItem(Class<? extends CustomItem> itemClass) {
        return customItems.stream().filter(c -> c.getClass() == itemClass).findFirst().orElse(null);
    }

    @Override
    public void addCustomItem(CustomItem item) {
        if(customItems.contains(item)) return;
        customItems.add(item);
    }

    @Override
    public void giveItemToPlayer(Class<? extends CustomItem> itemClass, UHCPlayer uhcPlayer) {
        CustomItem item = getCustomItem(itemClass);
        if(item == null) return;
        uhcPlayer.giveItem(item.getItemStack());
    }

    @EventHandler
    public void onClickItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player == null) return;
        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return;

        customItems.stream()
                .filter(item -> item.isItemStack(event.getItem()))
                .findFirst().ifPresent(item -> item.onClick(uhcPlayer));
    }
}
