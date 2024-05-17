package me.butter.impl.menu.list.noperms;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.timer.Timer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.StringUtils;
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
                return new ItemBuilder(Material.CHEST).setName("§rVoir l'inventaire de départ").toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new InventoryViewMenu(), true);
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
                return new ItemBuilder(Material.BOW).setName((UHCAPI.get().getGameHandler().getItemConfig().isBow() ? "§a" : "§c") + "Arc").toItemStack();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENDER_PEARL).setName((UHCAPI.get().getGameHandler().getItemConfig().isEnderPearl() ? "§a" : "§c") + "Ender Pearl").toItemStack();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FISHING_ROD).setName((UHCAPI.get().getGameHandler().getItemConfig().isRod() ? "§a" : "§c") + "Canne à pèche").toItemStack();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.SNOW_BALL).setName((UHCAPI.get().getGameHandler().getItemConfig().isProjectile() ? "§a" : "§c") + "Projectiles").toItemStack();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.LAVA_BUCKET).setName((UHCAPI.get().getGameHandler().getItemConfig().isLavaBucket() ? "§a" : "§c") + "Seau de lave").toItemStack();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FLINT_AND_STEEL).setName((UHCAPI.get().getGameHandler().getItemConfig().isFlintAndSteel() ? "§a" : "§c") + "Briquet").toItemStack();
            }
        });

        for(Timer timer : UHCAPI.get().getTimerHandler().getTimers()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    return new ItemBuilder(timer.getIcon()).setName(
                            "§r" + timer.getName() + ": " + StringUtils.convertToAccurateTime(timer.getMaxTimer())
                    ).toItemStack();
                }
            });
        }

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.APPLE).setName("§rPommes: " + UHCAPI.get().getGameHandler().getWorldConfig().getAppleDropRate() + "%").toItemStack();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.FLINT).setName("§rSilex: " + UHCAPI.get().getGameHandler().getWorldConfig().getFlintDropRate() + "%").toItemStack();
            }
        });

        buttons.add(new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.ENDER_PEARL).setName("§rEnder Pearls: " + UHCAPI.get().getGameHandler().getWorldConfig().getEnderPearlDropRate() + "%").toItemStack();
            }
        });

        return buttons;
    }
}
