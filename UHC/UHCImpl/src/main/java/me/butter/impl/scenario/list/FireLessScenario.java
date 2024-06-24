package me.butter.impl.scenario.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireLessScenario extends AbstractScenario {
    public FireLessScenario() {
        super("FireLess", Material.LAVA_BUCKET);
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {
        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            if(event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                    event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                    event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Désactive les dégats ",
                "issus du feu ou de la lave."
        };
    }
}
