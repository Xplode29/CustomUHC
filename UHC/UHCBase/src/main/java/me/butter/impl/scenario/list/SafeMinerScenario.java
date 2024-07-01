package me.butter.impl.scenario.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class SafeMinerScenario extends AbstractScenario {
    public SafeMinerScenario() {
        super("SafeMiner", Material.CHAINMAIL_CHESTPLATE);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Supprime les degats issus",
                "du feu ou de la lave",
                "et divise par 2 les degats recus",
                "en dessous de la couche 50"
        };
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(event.getEntity().getLocation().getY() < 50) {
                if(
                    event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                    event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                    event.getCause() == EntityDamageEvent.DamageCause.LAVA
                ) {
                    event.setCancelled(true);
                }
                else {
                    event.setDamage(event.getDamage() / 2);
                }
            }
        }
    }
}
