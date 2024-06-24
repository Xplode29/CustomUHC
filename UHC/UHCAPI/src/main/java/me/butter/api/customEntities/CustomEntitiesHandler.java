package me.butter.api.customEntities;

import java.util.List;

public interface CustomEntitiesHandler {

    List<CustomEntity> getCustomEntity();

    CustomEntity getCustomEntity(Class<? extends CustomEntity> entityClass);

    void registerCustomEntity(CustomEntity customEntity);
}
