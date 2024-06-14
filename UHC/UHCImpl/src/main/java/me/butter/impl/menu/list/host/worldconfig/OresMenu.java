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

import java.util.Map;

public class OresMenu extends AbstractMenu {
    public OresMenu() {
        super("Configuration des minerais", 5 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttonMap = super.getButtons();

        buttonMap.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.EXP_BOTTLE)
                        .setName("§rExp Boost: +" + UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() + "%")
                        .setLore("§7Clic gauche pour augmenter l'exp.", "§7Clic droit pour baisser.")
                        .toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
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
        }); // Exp Boost

        buttonMap.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIAMOND_ORE)
                        .setName("§rDiamond Limit: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit())
                        .setLore("§7Clic gauche pour augmenter la limite de diamants.", "§7Clic droit pour baisser.")
                        .toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
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
        }); // Diamond Limit

        return buttonMap;
    }
}
