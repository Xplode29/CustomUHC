package me.butter.impl.scenario.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BetaZombiesScenario extends AbstractScenario {
    public BetaZombiesScenario() {
        super("BetaZombies", Material.FEATHER);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Remplace la chair putrifiée",
                "des zombies par des plumes."
        };
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(event.getEntity() instanceof Zombie) {
                for(ItemStack item : new ArrayList<>(event.getDrops())) {
                    if(item.getType() == Material.ROTTEN_FLESH) {
                        event.getDrops().remove(item);
                        event.getDrops().add(new ItemStack(Material.FEATHER, item.getAmount()));
                    }
                }
            }
        }
    }
}
