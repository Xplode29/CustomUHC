package me.butter.impl.menu.list.host.worldconfig;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.list.host.worldconfig.border.BorderMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class WorldConfigMenu extends AbstractMenu {
    public WorldConfigMenu() {
        super("Configuration du monde", 6 * 9, true);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttonMap = super.getButtons();

        buttonMap.put(4, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.IRON_DOOR)
                        .setName("Changer de monde")
                        .setLore("§7Cliquez pour se teleporter à un monde.")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new WorldSelectMenu(), true);
            }
        }); // Select World

        buttonMap.put(11, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIAMOND_ORE)
                        .setName("Configuration des minerais")
                        .setLore("§7Cliquez pour configurer les minerais.")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new OresMenu(), true);
            }
        }); // Minerais

        buttonMap.put(15, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BARRIER)
                        .setName("Bordures")
                        .setLore("§7Cliquez pour modifier les bordures.")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new BorderMenu(), true);
            }
        }); // Bordure

        buttonMap.put(28, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.APPLE)
                        .setName("Drops")
                        .setLore("§7Cliquez pour modifier les drops.")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new DropsMenu(), true);
            }
        }); // Drops

        buttonMap.put(30, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.NETHERRACK)
                        .setName("Nether - " + (UHCAPI.getInstance().getGameHandler().getWorldConfig().isNetherActivated() ? "§aON" : "§cOFF"))
                        .setLore(
                            "§7Cliquez pour " +
                            (UHCAPI.getInstance().getGameHandler().getWorldConfig().isNetherActivated() ? "§cdesactiver" : "§aactiver") +
                            " §7le nether."
                        )
                        .build();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setNetherActivated(!UHCAPI.getInstance().getGameHandler().getWorldConfig().isNetherActivated());
            }
        }); // Nether

        buttonMap.put(32, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENDER_STONE)
                        .setName("End - " + (UHCAPI.getInstance().getGameHandler().getWorldConfig().isEnderActivated() ? "§aON" : "§cOFF"))
                        .setLore(
                            "§7Cliquez pour " +
                            (UHCAPI.getInstance().getGameHandler().getWorldConfig().isEnderActivated() ? "§cdesactiver" : "§aactiver") +
                            " §7l'end."
                        )
                        .build();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getGameHandler().getWorldConfig().setEnderActivated(!UHCAPI.getInstance().getGameHandler().getWorldConfig().isEnderActivated());
            }
        }); // End

        buttonMap.put(34, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DAYLIGHT_DETECTOR)
                        .setName("Cycle Jour-Nuit")
                        .setLore("§7Cliquez pour modifier le cycle jour-nuit.")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getMenuHandler().openMenu(player, new DayCycleMenu(), true);
            }
        }); // Day/Night Cycle

        buttonMap.put(47, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.DIRT)
                        .setName("Génération")
                        .setLore("§7Cliquez pour générer un nouveau monde.")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                UHCAPI.getInstance().getWorldHandler().createWorld("arena");
            }
        }); // World Generation

        buttonMap.put(51, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.LEAVES)
                        .setName("Prégénération")
                        .setLore("§7Cliquez pour lancer la pregeneration du monde.")
                        .build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                if(UHCAPI.getInstance().getGameHandler().getWorldConfig().isWorldGenerated()) {
                    UHCAPI.getInstance().getWorldHandler().loadWorld();
                }
                else {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Le monde n'est pas créé."));
                }
            }
        }); // World PreGeneration

        return buttonMap;
    }
}
