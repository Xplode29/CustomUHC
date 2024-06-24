package me.butter.impl.menu.list.player;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryViewMenu extends AbstractMenu {

    List<ItemStack> inventory;
    List<ItemStack> armor;

    public InventoryViewMenu(List<ItemStack> inventory, List<ItemStack> armor) {
        super("Inventaire", 6 * 9, false);

        this.inventory = inventory;
        this.armor = armor;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        for(int i = 0; i < this.inventory.size(); i++) {
            ItemStack itemStack = this.inventory.get(i);
            buttons.put((i < 9 ? i + 27 : i - 9), new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return itemStack;
                }
            });
        }

        for(int i = 36; i < 45; i++) {
            buttons.put(i, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.STAINED_GLASS_PANE).setName("").toItemStack();
                }
            });
        }

        for (int i = 50; i < 54; i++) {
            buttons.put(i, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.STAINED_GLASS_PANE).setName("").toItemStack();
                }
            });
        }

        for(int i = 0; i < this.armor.size(); i++) {
            ItemStack itemStack = this.armor.get(i);
            buttons.put(i + 45, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return itemStack;
                }
            });
        }

        return buttons;
    }
}
