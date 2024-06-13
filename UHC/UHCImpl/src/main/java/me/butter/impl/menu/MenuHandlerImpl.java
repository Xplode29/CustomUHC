package me.butter.impl.menu;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Menu;
import me.butter.api.menu.MenuHandler;
import me.butter.api.player.UHCPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuHandlerImpl implements MenuHandler, Listener {

    private Map<UUID, Menu> openedMenus;

    public MenuHandlerImpl() {
        this.openedMenus = new HashMap<>();

        //Register
        UHCAPI.getInstance().getServer().getPluginManager().registerEvents(this, UHCAPI.getInstance());
    }

    @Override
    public Map<UUID, Menu> getOpenedMenus() {
        return openedMenus;
    }

    @Override
    public Menu getOpenedMenu(UHCPlayer uhcPlayer) {
        return openedMenus.get(uhcPlayer.getUniqueId());
    }

    @Override
    public void openMenu(UHCPlayer uhcPlayer, Menu menu, boolean isPrevMenu) {
        Menu oldMenu = getOpenedMenu(uhcPlayer);
        openedMenus.remove(uhcPlayer.getUniqueId());
        menu.setOpener(uhcPlayer);
        openedMenus.put(uhcPlayer.getUniqueId(), menu);
        menu.open(oldMenu, isPrevMenu);
    }

    @Override
    public void closeMenu(UHCPlayer uhcPlayer) {
        for(UUID playerId : openedMenus.keySet()) {
            if(playerId.equals(uhcPlayer.getUniqueId())) {
                openedMenus.get(playerId).closeMenu();
                openedMenus.remove(playerId);
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer().getUniqueId());
        if(uhcPlayer == null) return;

        if(getOpenedMenu(uhcPlayer) != null && getOpenedMenu(uhcPlayer).getInventory().equals(event.getInventory())) {
            openedMenus.remove(uhcPlayer.getUniqueId());
        }
    }

    @EventHandler
    public void onClickButton(InventoryClickEvent event) {
        if(event.getAction() == InventoryAction.NOTHING) return;
        if(event.getInventory() == null) return;
        if(event.getClickedInventory() == null) return;
        if(!openedMenus.containsKey(event.getWhoClicked().getUniqueId())) return;
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        if(event.getCursor() != null && event.getCursor().getType() != Material.AIR) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getWhoClicked().getUniqueId());
        if(uhcPlayer == null) {
            event.setCancelled(true);
            return;
        }

        for(Menu menu : openedMenus.values()) {
            if(event.getClickedInventory().equals(menu.getInventory())) {
                menu.onClick(event.getSlot(), uhcPlayer, event.getClick());
                event.setCancelled(true);
                return;
            }

            if(uhcPlayer.getPlayer().getOpenInventory().getTopInventory().equals(menu.getInventory())) {
                if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
