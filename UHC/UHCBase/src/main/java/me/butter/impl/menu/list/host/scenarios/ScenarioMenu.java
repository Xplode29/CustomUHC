package me.butter.impl.menu.list.host.scenarios;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.scenario.Scenario;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScenarioMenu extends PaginatedMenu {

    boolean onlyEnabled = false;

    public ScenarioMenu() {
        super("Scenarios", 5 * 9);
    }

    @Override
    public Map<Integer, Button> getButtons() {
        Map<Integer, Button> buttons = super.getButtons();

        buttons.put(3, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                ItemBuilder builder = new ItemBuilder(Material.REDSTONE_BLOCK)
                        .setName("Liste des scenarios");
                if(!onlyEnabled) {
                    builder.addEnchant(Enchantment.DIG_SPEED, 1);
                    builder.hideEnchants();
                }
                return builder.toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                onlyEnabled = false;
                update();
            }
        });

        buttons.put(5, new ButtonImpl() {
            @Override
            public ItemStack getIcon() {
                ItemBuilder builder = new ItemBuilder(Material.REDSTONE_TORCH_ON)
                        .setName("Scenarios actives");
                if(onlyEnabled) {
                    builder.addEnchant(Enchantment.DIG_SPEED, 1);
                    builder.hideEnchants();
                }
                return builder.toItemStack();
            }

            @Override
            public void onClick(UHCPlayer player, ClickType clickType) {
                onlyEnabled = true;
                update();
            }
        });

        return buttons;
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();

        for(Scenario scenario : UHCAPI.getInstance().getScenarioHandler().getAllScenarios()) {
            if(onlyEnabled && !scenario.isEnabled()) continue;
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    ItemBuilder builder = new ItemBuilder(scenario.getIcon())
                            .setName("§r" + scenario.getName() + (scenario.isEnabled() ? " - §aActivé" : " - §cDésactivé"));

                    builder.addLoreLine("");
                    builder.addLoreLine(ChatUtils.ARROW + "Description:");
                    for(String line : scenario.getDescription()) {
                        builder.addLoreLine("§7" + line);
                    }
                    builder.addLoreLine("");
                    builder.addLoreLine(ChatUtils.ARROW + "§7Clic gauche pour " + (scenario.isEnabled() ? "§cdésactiver" : "§aactiver"));
                    builder.addLoreLine(ChatUtils.ARROW + "§7Clic droit pour configurer");

                    if(scenario.isEnabled()) {
                        builder.addEnchant(Enchantment.DIG_SPEED, 1)
                                .hideEnchants();
                    }

                    return builder.toItemStack();
                }

                @Override
                public boolean doesUpdateButton() {
                    return true;
                }

                @Override
                public boolean doesUpdateGui() {
                    return onlyEnabled;
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    if(clickType == ClickType.LEFT) {
                        scenario.toggle();
                        player.sendMessage(ChatUtils.GLOBAL_INFO.getMessage(
                                "Le scenario " + scenario.getName() + " est maintenant " + (scenario.isEnabled() ? "§aactivé !" : "§cdésactivé !")
                        ));
                    }
                    else if(clickType == ClickType.RIGHT) {
                        if(scenario.getConfigMenu() == null) {
                            player.sendMessage(ChatUtils.ERROR.getMessage(
                                    "Le scenario " + scenario.getName() + " n'a pas de configuration !"
                            ));
                            return;
                        }
                        player.openMenu(scenario.getConfigMenu(), true);
                    }
                }
            });
        }

        return buttons;
    }
}
