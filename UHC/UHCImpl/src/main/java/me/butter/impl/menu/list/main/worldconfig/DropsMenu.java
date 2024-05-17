package me.butter.impl.menu.list.main.worldconfig;

import me.butter.api.UHCAPI;
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

public class DropsMenu extends AbstractMenu {
    public DropsMenu() {
        super("Drops", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.APPLE).setName("§rPommes: " + UHCAPI.get().getGameHandler().getWorldConfig().getAppleDropRate() + "%").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.get().getGameHandler().getWorldConfig().getAppleDropRate() <= 95) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setAppleDropRate(UHCAPI.get().getGameHandler().getWorldConfig().getAppleDropRate() + 5);
                } else if(clickType == ClickType.RIGHT && UHCAPI.get().getGameHandler().getWorldConfig().getAppleDropRate() >= 5) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setAppleDropRate(UHCAPI.get().getGameHandler().getWorldConfig().getAppleDropRate() - 5);
                }
            }
        });

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FLINT).setName("§rSilex: " + UHCAPI.get().getGameHandler().getWorldConfig().getFlintDropRate() + "%").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.get().getGameHandler().getWorldConfig().getFlintDropRate() <= 95) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setFlintDropRate(UHCAPI.get().getGameHandler().getWorldConfig().getFlintDropRate() + 5);
                } else if(clickType == ClickType.RIGHT && UHCAPI.get().getGameHandler().getWorldConfig().getFlintDropRate() >= 5) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setFlintDropRate(UHCAPI.get().getGameHandler().getWorldConfig().getFlintDropRate() - 5);
                }
            }
        });

        buttons.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENDER_PEARL).setName("§rEnder Pearls: " + UHCAPI.get().getGameHandler().getWorldConfig().getEnderPearlDropRate() + "%").toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.get().getGameHandler().getWorldConfig().getEnderPearlDropRate() <= 95) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setEnderPearlDropRate(UHCAPI.get().getGameHandler().getWorldConfig().getEnderPearlDropRate() + 5);
                } else if(clickType == ClickType.RIGHT && UHCAPI.get().getGameHandler().getWorldConfig().getEnderPearlDropRate() >= 5) {
                    UHCAPI.get().getGameHandler().getWorldConfig().setEnderPearlDropRate(UHCAPI.get().getGameHandler().getWorldConfig().getEnderPearlDropRate() - 5);
                }
            }
        });

        return buttons;
    }
}
