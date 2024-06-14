package me.butter.impl.scenario.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

public class HasteyBoysScenario extends AbstractScenario {
    public HasteyBoysScenario() {
        super("Hastey Boys", Material.IRON_SPADE);
    }

    @EventHandler
    public void onObjectCraft(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        if(item == null) {
            return;
        }
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if((
                    item.toString().toLowerCase().contains("pickaxe") ||
                            item.toString().toLowerCase().contains("axe") ||
                            item.toString().toLowerCase().contains("spade")
            )) {
                if(item.getEnchantmentLevel(Enchantment.DIG_SPEED) < 3) {
                    item.addEnchantment(Enchantment.DIG_SPEED, 3);
                }
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Les outils sont directement enchantés ",
                "Efficacité III dès leur fabrication."
        };
    }
}
