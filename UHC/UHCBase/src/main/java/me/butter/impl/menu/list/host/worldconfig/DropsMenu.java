package me.butter.impl.menu.list.host.worldconfig;

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
                return new ItemBuilder(Material.APPLE).setName("§rPommes: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() + "%").toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() <= 95) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setAppleDropRate(UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() + 5);
                } else if(clickType == ClickType.RIGHT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() >= 5) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setAppleDropRate(UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() - 5);
                }
            }
        });

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FLINT).setName("§rSilex: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getFlintDropRate() + "%").toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getFlintDropRate() <= 95) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setFlintDropRate(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFlintDropRate() + 5);
                } else if(clickType == ClickType.RIGHT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getFlintDropRate() >= 5) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setFlintDropRate(UHCAPI.getInstance().getGameHandler().getWorldConfig().getFlintDropRate() - 5);
                }
            }
        });

        buttons.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENDER_PEARL).setName("§rEnder Pearls: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getEnderPearlDropRate() + "%").toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(clickType == ClickType.LEFT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getEnderPearlDropRate() <= 95) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setEnderPearlDropRate(UHCAPI.getInstance().getGameHandler().getWorldConfig().getEnderPearlDropRate() + 5);
                } else if(clickType == ClickType.RIGHT && UHCAPI.getInstance().getGameHandler().getWorldConfig().getEnderPearlDropRate() >= 5) {
                    UHCAPI.getInstance().getGameHandler().getWorldConfig().setEnderPearlDropRate(UHCAPI.getInstance().getGameHandler().getWorldConfig().getEnderPearlDropRate() - 5);
                }
            }
        });

        return buttons;
    }
}
