package me.butter.ninjago.goldenWeapons.configMenu;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.Icons;
import me.butter.ninjago.goldenWeapons.GoldenWeaponsScenario;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class StartSpawnWeaponsMenu extends AbstractMenu {

    private final GoldenWeaponsScenario scenario;

    public StartSpawnWeaponsMenu(GoldenWeaponsScenario scenario) {
        super("Debut de l'apparition des armes", 5 * 9, true);

        this.scenario = scenario;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(19, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_BIG.toItemStack("- 5min");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                scenario.setStartTimer(Math.max(10, scenario.getStartTimer() - 5 * 60));
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_MEDIUM.toItemStack("- 1min");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                scenario.setStartTimer(Math.max(10, scenario.getStartTimer() - 60));
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(21, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.MINUS_SMALL.toItemStack("- 10s");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                scenario.setStartTimer(Math.max(10, scenario.getStartTimer() - 10));
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(22, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.WATCH)
                        .setName("Debut de l'apparition des armes: " + GraphicUtils.convertToAccurateTime(scenario.getStartTimer()))
                        .toItemStack();
            }
        });

        buttons.put(23, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_SMALL.toItemStack("+ 10s");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                scenario.setStartTimer(Math.max(10, scenario.getStartTimer() + 10));
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_MEDIUM.toItemStack("+ 1min");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                scenario.setStartTimer(Math.max(10, scenario.getStartTimer() + 60));
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        buttons.put(25, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return Icons.PLUS_BIG.toItemStack("+ 5min");
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                scenario.setStartTimer(Math.max(10, scenario.getStartTimer() + 5 * 60));
            }

            @Override
            public boolean doesUpdateGui() {
                return true;
            }
        });

        return buttons;
    }
}
