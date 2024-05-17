package me.butter.impl.menu.list.main.worldconfig;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.StringUtils;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.Icons;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StartBorderMenu extends AbstractMenu {
    public StartBorderMenu() {
        super("Taille de la bordure initiale", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(19, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_BIG.toItemStack("- 500 blocks");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() - 500);
                if(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() < 500) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(500);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_MEDIUM.toItemStack("- 100 blocks");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() - 100);
                if(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() < 100) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(100);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(21, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_SMALL.toItemStack("- 50 blocks");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() - 50);
                if(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() < 50) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(50);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER).setName("§rTaille Initiale: " + UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize()).toItemStack();
            }
        });

        buttons.put(23, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_SMALL.toItemStack("+ 50 blocks");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() + 50);
                if(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() > 2000) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(2000);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_MEDIUM.toItemStack("+ 100 blocks");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() + 100);
                if(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() > 2000) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(2000);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(25, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_BIG.toItemStack("+ 500 blocks");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() + 500);
                if(UHCAPI.get().getGameHandler().getWorldConfig().getStartingBorderSize() > 2000) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setStartingBorderSize(2000);
                }
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        return buttons;
    }
}
