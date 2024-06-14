package me.butter.api.scenario;

import org.bukkit.Material;

public interface Scenario {

    String getName();

    String[] getDescription();

    Material getIcon();

    String getAuthor();

    String getDeveloper();

    void openConfig();

    void onEnable(); void onDisable();

    void toggle();

    boolean isEnabled(); void setEnabled(boolean enabled);
}
