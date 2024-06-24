package me.butter.api.scenario;

import me.butter.api.menu.Menu;
import org.bukkit.Material;

public interface Scenario {

    String getName();

    String[] getDescription();

    Material getIcon();

    String getAuthor();

    String getDeveloper();

    void onStartGame();

    void onUpdate(int timer);

    Menu getConfigMenu();

    void onEnable(); void onDisable();

    void toggle();

    boolean isEnabled(); void setEnabled(boolean enabled);
}
