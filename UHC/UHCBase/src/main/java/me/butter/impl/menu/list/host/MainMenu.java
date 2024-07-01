package me.butter.impl.menu.list.host;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.module.Module;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.host.StartCommand;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.list.host.presets.PresetsMenu;
import me.butter.impl.menu.list.host.scenarios.ScenarioMenu;
import me.butter.impl.menu.list.host.settings.SettingsMenu;
import me.butter.impl.menu.list.host.worldconfig.WorldConfigMenu;
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

        buttons.put(10, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON)
                        .setName("Presets")
                        .addLoreLine("§7Cliquez pour choisir un preset")
                        .toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new PresetsMenu(), true);
            }
        }); //Presets

        buttons.put(16, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BEACON)
                        .setName("Scenarios")
                        .addLoreLine("§7Cliquez pour voir les scenarios")
                        .toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new ScenarioMenu(), true);
            }
        }); //Scenarios

        if(UHCAPI.getInstance().getModuleHandler().hasModule()) {
            Module module = UHCAPI.getInstance().getModuleHandler().getModule();
            if(module.hasModuleMenu()) {
                buttons.put(22, new ButtonImpl() {
                    @Override
                    public ItemStack getIcon() {
                        return new ItemBuilder(module.getIcon())
                                .setName(module.getMainColor() + module.getName())
                                .addLoreLine("§7Cliquez pour ouvrir le menu du " + module.getName())
                                .toItemStack();
                    }

                    @Override
                    public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                        try {
                            uhcPlayer.openMenu(module.getMainMenuClass().newInstance(), true);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } //Module

        buttons.put(40, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder((UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting() ? Material.FIREBALL : Material.SLIME_BALL))
                        .setName("§r" + (UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting() ? "Arreter" : "Lancer"))
                        .toItemStack();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if (UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
                    UHCAPI.getInstance().reset();
                }
                else if(!UHCAPI.getInstance().getGameHandler().getWorldConfig().isWorldGenerated()) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Le monde arena n'est pas généré."));
                }
                else if(!UHCAPI.getInstance().getGameHandler().getWorldConfig().isPregenDone()) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Le monde arena n'est pas encore totalement prégené."));
                }
                else {
                    StartCommand.startGame();
                }
            }
        }); //Start/Stop

        buttons.put(47, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIRT)
                        .setName("Configuration du monde")
                        .addLoreLine("§7Cliquez pour ouvrir la configuration du monde")
                        .toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new WorldConfigMenu(), true);
            }
        }); //World Config

        buttons.put(51, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIODE)
                        .setName("Paramètres")
                        .addLoreLine("§7Cliquez pour ouvrir les paramètres")
                        .toItemStack();
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(uhcPlayer, new SettingsMenu(), true);
            }
        }); //Settings

        return buttons;
    }
}
