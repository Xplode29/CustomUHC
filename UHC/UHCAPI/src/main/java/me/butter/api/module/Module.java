package me.butter.api.module;

import me.butter.api.menu.Menu;
import me.butter.api.module.roles.Role;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Map;

public interface Module {

    String getName(); Material getIcon();

    String getVersion(); String getCreator();

    ChatColor getMainColor(); String getDoc();

    JavaPlugin getPlugin();

    boolean hasRoles();
    Map<Class<? extends Role>, Integer> getRoleComposition();
    List<Role> getRolesList(); void setRolesList(List<Role> roles);

    boolean hasTeams(); boolean hasCustomDeath();

    boolean hasModuleMenu(); Class<? extends Menu> getMainMenuClass();
}
