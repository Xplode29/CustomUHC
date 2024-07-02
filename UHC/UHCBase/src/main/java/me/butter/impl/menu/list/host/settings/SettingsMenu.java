package me.butter.impl.menu.list.host.settings;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.list.host.settings.inventory.InventoryMenu;
import me.butter.impl.menu.list.host.settings.timers.TimerMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SettingsMenu extends AbstractMenu {
    public SettingsMenu() {
        super("Paramètres de la partie", 6 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENCHANTED_BOOK)
                        .setName("Enchantements")
                        .addLoreLine("§7Cliquez pour modifier les enchantements")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new EnchantMenu(), true);
            }
        }); //Enchants

        buttons.put(15, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.WATCH)
                        .setName("Timers")
                        .addLoreLine("§7Cliquez pour modifier les timers")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new TimerMenu(), true);
            }
        }); //Timers

        buttons.put(28, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.POTION)
                        .setName("Potions")
                        .addLoreLine("§7Cliquez pour modifier les potions")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new PotionMenu(), true);
            }
        }); //Potions

        buttons.put(30, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIAMOND_HELMET)
                        .setName("Limites de stuff")
                        .addLoreLine("§7Cliquez pour modifier les limites de stuff")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                //UHCAPI.getInstance().getMenuHandler().openMenu(player, new TimerMenu(), true);
            }
        }); //Equipement (menu pas fait)

        buttons.put(32, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BOOK_AND_QUILL)
                        .setName("Items autorisés")
                        .addLoreLine("§7Cliquez pour modifer les items autorisés")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(uhcPlayer, new AuthorizedItemsMenu(), true);
            }
        }); //Rules

        buttons.put(34, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.CHEST)
                        .setName("Inventaire")
                        .addLoreLine("§7Cliquez pour modifer l'inventaire de départ")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new InventoryMenu(), true);
            }
        }); //Inventory

        return buttons;
    }
}
