package me.butter.ninjago.goldenWeapons.configMenu;

import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.ninjago.goldenWeapons.GoldenWeaponsScenario;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GoldenWeaponConfigMenu extends AbstractMenu {

    private final GoldenWeaponsScenario scenario;

    public GoldenWeaponConfigMenu(GoldenWeaponsScenario scenario) {
        super("Configuration des armes d'or", 5 * 9, true);

        this.scenario = scenario;
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(20, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.WATCH)
                        .setName("Apparition des armes: " + GraphicUtils.convertToAccurateTime(scenario.getStartTimer()))
                        .build();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new StartSpawnWeaponsMenu(scenario), true);
            }
        }); // Start Timer

        buttons.put(24, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                return new ItemBuilder(Material.WATCH)
                        .setName("Temps entre chaque apparition: " + GraphicUtils.convertToAccurateTime(scenario.getTimeBetweenSpawn()))
                        .build();
            }

            @Override
            public boolean doesUpdateButton() {
                return true;
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                player.openMenu(new CooldownSpawnWeaponsMenu(scenario), true);
            }
        }); // Time between spawn

        return buttons;
    }
}
