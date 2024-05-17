package me.butter.impl.menu.list.main;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.list.main.worldconfig.DropsMenu;
import me.butter.impl.menu.list.main.settings.EnchantMenu;
import me.butter.impl.menu.list.main.settings.TimerMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends AbstractMenu {
    public SettingsMenu() {
        super("Paramètres de la partie", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENCHANTED_BOOK).setName("§rEnchantements").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getMenuHandler().openMenu(player, new EnchantMenu(), true);
            }
        });

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.WATCH).setName("§rTimers").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getMenuHandler().openMenu(player, new TimerMenu(), true);
            }
        });

        return buttons;
    }
}
