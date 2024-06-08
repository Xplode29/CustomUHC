package me.butter.ninjago;

import me.butter.api.menu.Menu;
import me.butter.api.module.Module;
import me.butter.api.module.roles.Role;
import me.butter.api.module.roles.RoleType;
import me.butter.ninjago.menu.MainNinjagoMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class NinjagoModule implements Module {

    @Override
    public String getName() {
        return "Ninjago UHC";
    }

    @Override
    public Material getIcon() {
        return Material.GOLD_SWORD;
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getCreator() {
        return "ButterOnPancakes";
    }

    @Override
    public ChatColor getMainColor() {
        return ChatColor.RED;
    }

    @Override
    public String getDoc() {
        return "https://butteronpancakes.gitbook.io/ninjago-uhc";
    }

    @Override
    public JavaPlugin getPlugin() {
        return Ninjago.getInstance();
    }

    @Override
    public boolean hasRoles() {
        return true;
    }

    @Override
    public List<RoleType> getRoleComposition() {
        return Ninjago.getInstance().getRolesComposition();
    }

    @Override
    public List<Role> getRolesList() {
        return Ninjago.getInstance().getRolesList();
    }

    @Override
    public void setRolesList(List<Role> roles) {
        Ninjago.getInstance().setRolesList(roles);
    }

    @Override
    public boolean hasTeams() {
        return false;
    }

    @Override
    public boolean hasCustomDeath() {
        return false;
    }

    @Override
    public boolean hasModuleMenu() {
        return true;
    }

    @Override
    public Class<? extends Menu> getMainMenuClass() {
        return MainNinjagoMenu.class;
    }
}
