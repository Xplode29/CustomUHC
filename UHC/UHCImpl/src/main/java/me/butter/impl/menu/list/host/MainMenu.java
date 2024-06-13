package me.butter.impl.menu.list.host;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.menu.Button;
import me.butter.api.module.Module;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.host.StartCommand;
import me.butter.impl.commands.host.StopCommand;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
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

        buttons.put(4, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.REDSTONE_TORCH_ON).setName("§rPresets").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new PresetsMenu(), true);
            }
        });

        buttons.put(11, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.CHEST).setName("§rInventaire").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new InventoryMenu(), true);
            }
        });

        buttons.put(15, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BEACON).setName("§rScenarios").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new ScenarioMenu(), true);
            }
        });

        buttons.put(19, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BOOK_AND_QUILL).setName("§rRègles").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(uhcPlayer, new RulesMenu(), true);
            }
        });

        buttons.put(25, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIODE).setName("§rParamètres").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer uhcPlayer, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(uhcPlayer, new SettingsMenu(), true);
            }
        });

        buttons.put(38, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIRT).setName("§rConfiguration du monde").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new WorldConfigMenu(), true);
            }
        });

        if(UHCAPI.getInstance().getModuleHandler().hasModule()) {
            Module module = UHCAPI.getInstance().getModuleHandler().getModule();
            if(module.hasModuleMenu()) {
                buttons.put(42, new ButtonImpl() {
                    @Override
                    public ItemStack getIcon() {
                        return new ItemBuilder(module.getIcon())
                                .setName(module.getMainColor() + module.getName())
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
        }

        buttons.put(49, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder((UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting() ? Material.FIREBALL : Material.SLIME_BALL))
                        .setName("§r" + (UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting() ? "Arreter" : "Lancer"))
                        .toItemStack();
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if (UHCAPI.getInstance().getGameHandler().getGameConfig().isStarting()) {
                    StopCommand.stopGame();
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
        });

        return buttons;
    }
}
