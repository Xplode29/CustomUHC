package me.butter.impl.menu.list.host;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.list.host.worldconfig.DayCycleMenu;
import me.butter.impl.menu.list.host.worldconfig.FinalBorderMenu;
import me.butter.impl.menu.list.host.worldconfig.StartBorderMenu;
import me.butter.impl.menu.list.host.worldconfig.WorldSelectMenu;
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

        buttonMap.put(4, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.IRON_DOOR).setName("§rSe Téléporter à un monde").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new WorldSelectMenu(), true);
            }
        });

        buttonMap.put(19, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER).setName("§rBorder Initiale: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize()).toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new StartBorderMenu(), true);
            }
        });

        buttonMap.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER).setName("§rBorder Finale: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize()).toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new FinalBorderMenu(), true);
            }
        });

        buttonMap.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DAYLIGHT_DETECTOR).setName("§rCycle Jour-Nuit").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new DayCycleMenu(), true);
            }
        });

        buttonMap.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIAMOND_ORE).setName("§rDiamond Limit: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit()).toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit() < 30) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setDiamondLimit(UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit() + 1);
                } else if(clickType == ClickType.RIGHT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit() > 0) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setDiamondLimit(UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit() - 1);
                }
            }
        });

        buttonMap.put(25, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.EXP_BOTTLE).setName("§rExp Boost: +" + UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() + "%").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() <= 95) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setExpBoost(UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() + 5);
                } else if(clickType == ClickType.RIGHT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() >= 5) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setExpBoost(UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() - 5);
                }
            }
        });

        buttonMap.put(39, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIRT).setName("§rGénération").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getWorldHandler().createWorld("arena");
            }
        });

        buttonMap.put(41, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.LEAVES).setName("§rPrégénération").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(UHCAPI.getInstance().getGameHandler().getWorldConfig().isWorldGenerated()) {
                    UHCAPI.getInstance().getWorldHandler().loadWorld();
                }
                else {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Le monde n'est pas créé."));
                }
            }
        });

        return buttonMap;
    }
}
