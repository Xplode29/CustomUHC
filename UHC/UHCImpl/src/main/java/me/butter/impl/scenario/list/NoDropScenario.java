package me.butter.impl.scenario.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.impl.events.custom.CustomBlockBreakEvent;
import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class NoDropScenario extends AbstractScenario {
    public NoDropScenario() {
        super("NoDrop", Material.DIAMOND_PICKAXE);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(CustomBlockBreakEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(
                    event.getBlockBroken().getType() == Material.LAPIS_ORE ||
                            event.getBlockBroken().getType() == Material.GOLD_ORE ||
                            event.getBlockBroken().getType() == Material.IRON_ORE ||
                            event.getBlockBroken().getType() == Material.DIAMOND_ORE ||
                            event.getBlockBroken().getType() == Material.EMERALD_ORE
            ) {
                for(ItemStack item : event.getDrops()) {
                    if(
                            event.getUhcPlayer().getPlayer().getInventory().firstEmpty() > -1 ||
                                    event.getUhcPlayer().getPlayer().getInventory().contains(item)
                    ) {
                        event.getUhcPlayer().giveItem(item, false);
                    }
                    else {
                        event.getBlockBroken().getLocation().getWorld().dropItem(event.getBlockBroken().getLocation(), item);
                    }
                }
                event.getUhcPlayer().getPlayer().giveExp(event.getExpToDrop());
                event.setDrops(new ArrayList<>());
                event.setExpToDrop(0);
            }
        }
    }
}
