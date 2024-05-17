package me.butter.impl.menu.list;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.list.main.*;
import me.butter.impl.task.LaunchGameTask;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class MainMenu extends AbstractMenu {
    public MainMenu() {
        super("Main Menu", 6 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.CHEST).setName("§rInventaire").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getMenuHandler().openMenu(player, new InventoryMenu(), true);
            }
        });

        buttons.put(15, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BEACON).setName("§rScenarios").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getMenuHandler().openMenu(player, new ScenarioMenu(), true);
            }
        });

        buttons.put(19, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BOOK_AND_QUILL).setName("§rRègles").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                UHCAPI.get().getMenuHandler().openMenu(uhcPlayer, new RulesMenu(), true);
            }
        });

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemStack(Material.IRON_CHESTPLATE);
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                uhcPlayer.sendMessage("GameMode Panel");
            }
        });

        buttons.put(25, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIODE).setName("§rParamètres").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                UHCAPI.get().getMenuHandler().openMenu(uhcPlayer, new SettingsMenu(), true);
            }
        });

        buttons.put(38, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.COAL_ORE).setName("§rConfiguration des minerais").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new WorldConfigMenu(), true);
            }
        });

        buttons.put(42, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.LEAVES).setName("§rPrégénération").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.get().getWorldHandler().loadWorld();
            }
        });

        buttons.put(49, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemStack((UHCAPI.get().getGameHandler().getGameConfig().isStarting() ? Material.FIREBALL : Material.SLIME_BALL));
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if (UHCAPI.get().getGameHandler().getGameConfig().isStarting()) {
                    UHCAPI.get().getGameHandler().getGameConfig().setStarting(false);
                }
                else {
                    UHCAPI.get().getGameHandler().getGameConfig().setStarting(true);
                    new LaunchGameTask();
                }
            }
        });

        return buttons;
    }
}
