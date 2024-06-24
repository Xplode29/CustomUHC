package me.butter.impl.customEntities;

import me.butter.api.UHCAPI;
import me.butter.api.customEntities.CustomEntitiesHandler;
import me.butter.api.customEntities.CustomEntity;
import net.minecraft.server.v1_8_R3.EntityCreature;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomEntitiesHandlerImpl implements CustomEntitiesHandler, Listener {

    List<CustomEntity> customEntities;

    public CustomEntitiesHandlerImpl() {
        customEntities = new ArrayList<>();

        Bukkit.getPluginManager().registerEvents(this, UHCAPI.getInstance());
    }

    @Override
    public List<CustomEntity> getCustomEntity() {
        return customEntities;
    }

    @Override
    public CustomEntity getCustomEntity(Class<? extends CustomEntity> entityClass) {
        for(CustomEntity customEntity : customEntities) {
            if(customEntity.getClass() == entityClass) {
                return customEntity;
            }
        }
        return null;
    }

    @Override
    public void registerCustomEntity(CustomEntity customEntity) {
        if(!customEntities.contains(customEntity))
            customEntities.add(customEntity);
    }

    @EventHandler
    public void onEntityDies(EntityDeathEvent event) {
        if(!(event.getEntity() instanceof EntityCreature)) return;

        EntityCreature entity = (EntityCreature) event.getEntity();
        for(CustomEntity customEntity : customEntities) {
            if(customEntity.getEntity() == entity) {
                customEntity.onEntityDeath();
            }
        }
    }
}
