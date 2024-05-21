package me.butter.impl.menu.list.host.worldconfig;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.Icons;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class FinalBorderMenu extends AbstractMenu {
    public FinalBorderMenu() {
        super("Taille de la bordure finale", 5 * 9, true);
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
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() - 500);
                if(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() < 100) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(100);
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
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() - 100);
                if(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() < 100) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(100);
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
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() - 50);
                if(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() < 100) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(100);
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
                return new ItemBuilder(Material.BARRIER).setName("§rTaille Finale: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize()).toItemStack();
            }
        });

        buttons.put(23, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_SMALL.toItemStack("+ 50 blocks");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() + 50);
                if(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() > 2000) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(2000);
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
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() + 100);
                if(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() > 2000) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(2000);
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
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() + 500);
                if(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFinalBorderSize() > 2000) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setFinalBorderSize(2000);
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
