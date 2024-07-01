package me.butter.impl.scenario;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Menu;
import me.butter.api.scenario.Scenario;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class AbstractScenario implements Scenario, Listener {

    private String name;
    private Material icon;
    private boolean enabled;

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
    public Material getIcon() {
        return icon;
    }

    @Override
    public String getAuthor() {
        return "";
    }

    @Override
    public String getDeveloper() {
        return "ButterOnPancakes";
    }

    @Override
    public void onStartGame() {

    }

    @Override
    public void onUpdate(int timer) {

    }

    @Override
    public Menu getConfigMenu() {
        return null;
    }

    @Override
    public void onEnable() {
        UHCAPI.getInstance().getServer().getPluginManager().registerEvents(this, UHCAPI.getInstance());
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void toggle() {
        enabled = !enabled;

        if (enabled) onEnable();
        else onDisable();
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
