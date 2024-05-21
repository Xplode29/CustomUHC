package me.butter.ninjago.menu;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MainNinjagoMenu extends AbstractMenu {
    public MainNinjagoMenu() {
        super("Ninjago UHC", 6 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.CHEST).setName("§rTest").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.sendMessage("Test");
            }
        });

        return buttons;
    }
}
