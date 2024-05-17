package me.butter.impl.menu.list.main;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.list.main.worldconfig.DropsMenu;
import me.butter.impl.menu.list.main.worldconfig.FinalBorderMenu;
import me.butter.impl.menu.list.main.worldconfig.StartBorderMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class WorldConfigMenu extends AbstractMenu {
    public WorldConfigMenu() {
        super("Configuration du monde", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttonMap = super.getButtons();

        buttonMap.put(19, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER).setName("§rBorder Initiale").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new StartBorderMenu(), true);
            }
        });

        buttonMap.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER).setName("§rBorder Finale").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new FinalBorderMenu(), true);
            }
        });

        buttonMap.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.APPLE).setName("§rDrops").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getMenuHandler().openMenu(player, new DropsMenu(), true);
            }
        });

        buttonMap.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIAMOND_ORE).setName("§rDiamond Limit: " + UHCAPI.get().getGameHandler().getWorldConfig().getDiamondLimit()).toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.get().getGameHandler().getWorldConfig().getDiamondLimit() < 30) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setDiamondLimit(UHCAPI.get().getGameHandler().getWorldConfig().getDiamondLimit() + 1);
                } else if(clickType == ClickType.RIGHT && UHCAPI.get().getGameHandler().getWorldConfig().getDiamondLimit() > 0) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setDiamondLimit(UHCAPI.get().getGameHandler().getWorldConfig().getDiamondLimit() - 1);
                }
            }
        });

        buttonMap.put(25, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.EXP_BOTTLE).setName("§rExp Boost: +" + UHCAPI.get().getGameHandler().getWorldConfig().getExpBoost() + "%").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.get().getGameHandler().getWorldConfig().getExpBoost() <= 95) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setExpBoost(UHCAPI.get().getGameHandler().getWorldConfig().getExpBoost() + 5);
                } else if(clickType == ClickType.RIGHT && UHCAPI.get().getGameHandler().getWorldConfig().getExpBoost() >= 5) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setExpBoost(UHCAPI.get().getGameHandler().getWorldConfig().getExpBoost() - 5);
                }
            }
        });

        return buttonMap;
    }
}
