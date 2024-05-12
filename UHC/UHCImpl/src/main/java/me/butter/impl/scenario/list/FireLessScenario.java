package me.butter.impl.scenario.list;

import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireLessScenario extends AbstractScenario {
    public FireLessScenario() {
        super("FireLess", Material.FLINT_AND_STEEL);
    }

    @EventHandler
    public void onTakeDamage(EntityDamageEvent event) {
        if(event.getCause() == EntityDamageEvent.DamageCause.FIRE ||
                event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK ||
                event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
        }
    }
}
