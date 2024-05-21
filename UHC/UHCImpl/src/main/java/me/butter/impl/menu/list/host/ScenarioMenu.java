package me.butter.impl.menu.list.host;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.player.UHCPlayer;
import me.butter.api.scenario.Scenario;
import me.butter.api.utils.ChatUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.impl.menu.ButtonImpl;
import me.butter.impl.menu.PaginatedMenu;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ScenarioMenu extends PaginatedMenu {

    public ScenarioMenu() {
        super("Scenarios", 5 * 9);
    }

    @Override
    public List<Button> getAllButtons() {
        List<Button> buttons = new ArrayList<>();

        for(Scenario scenario : UHCAPI.getInstance().getScenarioHandler().getAllScenarios()) {
            buttons.add(new ButtonImpl() {
                @Override
                public ItemStack getIcon() {
                    if(scenario.isEnabled()) {
                        return new ItemBuilder(scenario.getIcon())
                                .setName("§r" + scenario.getName())
                                .addEnchant(Enchantment.DIG_SPEED, 1)
                                .hideEnchants()
                                .toItemStack();
                    }
                    return new ItemBuilder(scenario.getIcon())
                            .setName("§r" + scenario.getName())
                            .toItemStack();
                }

                @Override
                public boolean doesUpdateGui() {
                    return true;
                }

                @Override
                public void onClick(UHCPlayer player, ClickType clickType) {
                    scenario.toggle();
                    player.sendMessage(ChatUtils.GLOBAL_INFO.getMessage(
                            "Le scenario " + scenario.getName() + " est maintenant " + (scenario.isEnabled() ? "§aactivé !" : "§cdésactivé !")
                    ));
                }
            });
        }

        return buttons;
    }
}
