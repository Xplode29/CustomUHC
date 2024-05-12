package me.butter.impl.scenario;

import me.butter.api.UHCAPI;
import me.butter.api.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class AbstractScenario implements Scenario, Listener {

    boolean enabled;
    String name;
    Material icon;

    public AbstractScenario(String name, Material icon) {
        this.name = name;
        this.icon = icon;
        this.enabled = false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public Material getIcon() {
        return icon;
    }

    @Override
    public void onEnable() {
        UHCAPI.get().getServer().getPluginManager().registerEvents(this, UHCAPI.get());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void openConfig() {

    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
