package me.butter.impl.menu.list.host.settings;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.Enchant;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantMenu extends PaginatedMenu {
    List<Button> buttons;

    public EnchantMenu() {
        super("Enchantements", 5 * 9);

        buttons = new ArrayList<>();

        for(Enchant enchant : UHCAPI.getInstance().getEnchantHandler().getEnchants()) {
            buttons.add(new EnchantButton(enchant));
        }
    }

    @Override
    public List<Button> getAllButtons() {
        return buttons;
    }

    private static class EnchantButton extends ButtonImpl {
        public Enchant enchant;
        int selected = 0;

        public EnchantButton(Enchant enchant) {
            this.enchant = enchant;
        }

        @Override
        public ItemStack getIcon() {
            return new ItemBuilder((enchant.getIronLevel() + enchant.getDiamondLevel() > 0 ? Material.ENCHANTED_BOOK : Material.BOOK))
                    .setName("§r" + enchant.getName())
                    .addLoreLine((selected == 0 ? "§a" : "§7") + "Fer: " + enchant.getIronLevel() + " / " + enchant.getMaxLevel())
                    .addLoreLine((selected == 1 ? "§a" : "§7") + "Diamant: " + enchant.getDiamondLevel() + " / " + enchant.getMaxLevel())
                    .toItemStack();
        }

        @Override
        public boolean doesUpdateButton() {
            return true;
        }

        @Override
        public void onClick(UHCPlayer player, ClickType clickType) {
            if (clickType == ClickType.LEFT) {
                if(selected == 0) {
                    if(enchant.getIronLevel() < enchant.getMaxLevel()) {
                        enchant.setIronLevel(enchant.getIronLevel() + 1);
                    }
                    else {
                        enchant.setIronLevel(0);
                    }
                }
                else if(selected == 1) {
                    if(enchant.getDiamondLevel() < enchant.getMaxLevel()) {
                        enchant.setDiamondLevel(enchant.getDiamondLevel() + 1);
                    }
                    else {
                        enchant.setDiamondLevel(0);
                    }
                }
            }
            else if(clickType == ClickType.RIGHT)  {
                selected++;
                if(selected > 1) selected = 0;
            }
        }
    }
}
