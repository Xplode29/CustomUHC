package me.butter.impl.menu.list.host.worldconfig.border;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BorderMenu extends AbstractMenu {
    public BorderMenu() {
        super("Configuration des minerais", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttonMap = super.getButtons();

        buttonMap.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER)
                        .setName("§rBorder Initiale: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize())
                        .setLore("§7Cliquez pour modifier la taille de la bordure initiale.")
                        .toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new StartBorderMenu(), true);
            }
        }); // Start Border

        buttonMap.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER)
                        .setName("§rBorder Finale: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize())
                        .setLore("§7Cliquez pour modifier la taille de la bordure finale.")
                        .toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new FinalBorderMenu(), true);
            }
        }); // Final Border

        return buttonMap;
    }
}
