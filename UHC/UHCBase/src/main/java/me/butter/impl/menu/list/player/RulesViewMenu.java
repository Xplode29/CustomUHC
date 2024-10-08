package me.butter.impl.menu.list.player;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.timer.Timer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RulesViewMenu extends PaginatedMenu {
    public RulesViewMenu() {
        super("Règles", 6 * 9);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttonMap = super.getButtons();
        buttonMap.put(4, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.CHEST).setName("§rVoir l'inventaire de départ").build();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new InventoryViewMenu(UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingInventory(), UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getStartingArmor()), true);
            }
        });
        return buttonMap;
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.BOW).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isBow() ? "§a" : "§c") + "Arc").build();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENDER_PEARL).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isEnderPearl() ? "§a" : "§c") + "Ender Pearl").build();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FISHING_ROD).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isRod() ? "§a" : "§c") + "Canne à pèche").build();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.SNOW_BALL).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isProjectile() ? "§a" : "§c") + "Projectiles").build();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.LAVA_BUCKET).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isLavaBucket() ? "§a" : "§c") + "Seau de lave").build();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FLINT_AND_STEEL).setName((UHCAPI.getInstance().getGameHandler().getItemConfig().isFlintAndSteel() ? "§a" : "§c") + "Briquet").build();
            }
        });

        for(Timer timer : UHCAPI.getInstance().getTimerHandler().getTimers()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(timer.getIcon()).setName(
                            "§r" + timer.getName() + ": " + GraphicUtils.convertToAccurateTime(timer.getMaxTimer())
                    ).build();
                }
            });
        }

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.APPLE).setName("§rPommes: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() + "%").build();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FLINT).setName("§rSilex: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getFlintDropRate() + "%").build();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENDER_PEARL).setName("§rEnder Pearls: " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getEnderPearlDropRate() + "%").build();
            }
        });

        return buttons;
    }
}
