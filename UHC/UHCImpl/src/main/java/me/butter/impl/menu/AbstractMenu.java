package me.butter.impl.menu;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.menu.Menu;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractMenu implements Menu {

    Map<Integer, Button> buttons;
    Inventory inventory;
    String title;
    int size;

    boolean updateOnClick, hasGlass;
    UHCPlayer opener;

    Menu previousMenu;

    public AbstractMenu(String title, int size, boolean hasGlass) {
        this.title = title;
        this.size = size;
        this.buttons = new HashMap<>();
        this.updateOnClick = false;
        this.hasGlass = hasGlass;
    }

    @Override
    public void update() {
        if(opener == null) return;
        if(opener.getPlayer() == null) return;

        inventory.clear();

        if(hasGlass) {
            for (int i : new int[]{0, 1, 7, 8, 9, 17, size - 18, size - 10, size - 9, size - 8, size - 2, size - 1}) {
                this.inventory.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").toItemStack());
            }
        }

        if(previousMenu != null) {
            this.inventory.setItem(
                size - 5, new ItemBuilder(Material.ARROW).setName("§rRetour").toItemStack()
            );
        }

        for (Map.Entry<Integer, Button> buttonEntry : getButtons().entrySet()) {
            this.inventory.setItem(
                    buttonEntry.getKey(), buttonEntry.getValue().getIcon()
            );
        }
    }

    @Override
    public void open(Menu previousMenu, boolean isPrevMenu) {
        if(opener == null) return;
        if(opener.getPlayer() == null) return;

        if(this.previousMenu == null && isPrevMenu) {
            this.previousMenu = previousMenu;
        }

        inventory = Bukkit.createInventory(opener.getPlayer(), size, title);
        update();

        opener.getPlayer().openInventory(getInventory());
    }

    @Override
    public void closeMenu() {
        if(opener.getPlayer() == null) return;
        opener.getPlayer().closeInventory();
    }

    @Override
    public void onClick(int slot, UHCPlayer uhcPlayer, ClickType clickType) {
        Button button = getButtons().get(slot);
        if(button != null) {
            button.onClick(uhcPlayer, clickType);
            if(button.doesUpdateGui()) {
                update();
            }
            return;
        }

        if(previousMenu != null && slot == size - 5) {
            UHCAPI.getInstance().getMenuHandler().openMenu(uhcPlayer, previousMenu, false);
        }
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        return new HashMap<>();
    }

    @Override
    public void setButtons(Map<Integer, Button> buttons) {
        this.buttons = buttons;
    }

    @Override
    public UHCPlayer getOpener() {
        return opener;
    }

    @Override
    public void setOpener(UHCPlayer opener) {
        this.opener = opener;
    }

    @Override
    public boolean doesUpdateOnClick() {
        return updateOnClick;
    }

    @Override
    public void setUpdateOnClick(boolean updateOnClick) {
        this.updateOnClick = updateOnClick;
    }
}
