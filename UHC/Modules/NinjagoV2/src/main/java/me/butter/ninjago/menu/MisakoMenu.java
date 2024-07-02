package me.butter.ninjago.menu;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MisakoMenu extends AbstractMenu {

    UHCPlayer uhcPlayer;

    public MisakoMenu(UHCPlayer uhcPlayer) {
        super("Inventaire de " + uhcPlayer.getName(), 6 * 9, false);

        this.uhcPlayer = uhcPlayer;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        for(int i = 0; i < uhcPlayer.getInventory().size(); i++) {
            ItemStack itemStack = uhcPlayer.getInventory().get(i);
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
                    return new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").build();
                }
            });
        }

        for (int i = 50; i < 54; i++) {
            buttons.put(i, new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(Material.STAINED_GLASS_PANE).setName(" ").build();
                }
            });
        }

        for(int i = 0; i < uhcPlayer.getArmor().size(); i++) {
            ItemStack itemStack = uhcPlayer.getArmor().get(i);
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
