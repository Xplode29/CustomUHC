package me.butter.api.scenario;

import org.bukkit.Material;

public interface Scenario {

    String getName();

    String[] getDescription();

    Material getIcon();

    void onEnable(); void onDisable();

    void openConfig();

    boolean isEnabled(); void setEnabled(boolean enabled);
}
