package me.butter.ninjago.menu;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.ninjago.roles.CampEnum;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class CampMenu extends AbstractMenu {
    public CampMenu() {
        super("Camps", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(19, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(CampEnum.NINJA.getIcon()).setName("§rNinja").build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new RoleMenu(CampEnum.NINJA), true);
            }
        });

        buttons.put(21, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(CampEnum.SNAKE.getIcon()).setName("§rSerpents").build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new RoleMenu(CampEnum.SNAKE), true);
            }
        });

        buttons.put(23, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(CampEnum.MASTER.getIcon()).setName("§rMaitres").build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new RoleMenu(CampEnum.MASTER), true);
            }
        });

        buttons.put(25, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(CampEnum.SOLO.getIcon()).setName("§rSolitaires").build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new RoleMenu(CampEnum.SOLO), true);
            }
        });

        return buttons;
    }
}
