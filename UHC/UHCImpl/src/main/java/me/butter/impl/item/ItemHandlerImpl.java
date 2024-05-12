package me.butter.impl.item;

import me.butter.api.UHCAPI;
import me.butter.api.item.CustomItem;
import me.butter.api.item.ItemHandler;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.item.list.GrapplingItem;
import me.butter.impl.item.list.MenuItem;
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

        customItems.add(new MenuItem());
        customItems.add(new GrapplingItem());

        //Register
        UHCAPI.get().getServer().getPluginManager().registerEvents(this, UHCAPI.get());

        for(CustomItem item : customItems) {
            if(item instanceof Listener) UHCAPI.get().getServer().getPluginManager().registerEvents((Listener) item, UHCAPI.get());
        }
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

    @Override
    public void giveItemToPlayer(Class<? extends CustomItem> itemClass, int slot, UHCPlayer uhcPlayer) {
        CustomItem item = getCustomItem(itemClass);
        if(item == null) return;
        uhcPlayer.setItem(slot, item.getItemStack());
    }

    @Override
    public void removeItemFromPlayer(Class<? extends CustomItem> itemClass, UHCPlayer uhcPlayer) {
        CustomItem item = getCustomItem(itemClass);
        if(item == null) return;
        for(ItemStack itemStack : uhcPlayer.getPlayer().getInventory().getContents()) {
            if(itemStack == null) continue;
            if(item.getItemStack().isSimilar(itemStack)) {
                uhcPlayer.getPlayer().getInventory().remove(itemStack);
            }
        }
    }

    @Override
    public void giveLobbyItems(UHCPlayer uhcPlayer) {
        if(UHCAPI.get().getGameHandler().getGameConfig().getHost() != null && UHCAPI.get().getGameHandler().getGameConfig().getHost().equals(uhcPlayer)) {
            giveItemToPlayer(MenuItem.class, uhcPlayer);
        }
        giveItemToPlayer(GrapplingItem.class, uhcPlayer);
    }

    @Override
    public void removeLobbyItems(UHCPlayer uhcPlayer) {
        if(UHCAPI.get().getGameHandler().getGameConfig().getHost() != null && UHCAPI.get().getGameHandler().getGameConfig().getHost().equals(uhcPlayer)) {
            removeItemFromPlayer(MenuItem.class, uhcPlayer);
        }
        removeItemFromPlayer(GrapplingItem.class, uhcPlayer);
    }

    @EventHandler
    public void onClickItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player == null) return;
        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return;

        customItems.stream()
                .filter(item -> item.getItemStack().isSimilar(event.getItem()))
                .findFirst().ifPresent(item -> item.onClick(uhcPlayer));
    }
}
