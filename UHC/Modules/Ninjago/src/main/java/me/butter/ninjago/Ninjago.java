package me.butter.ninjago;

import me.butter.api.UHCAPI;
import me.butter.api.module.roles.Role;
import me.butter.ninjago.commands.CommandNinjago;
import me.butter.ninjago.listener.CycleEvents;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Ninjago extends JavaPlugin {

    private static Ninjago instance;

    private Map<Class<? extends Role>, Integer> rolesComposition;
    private List<Role> rolesList;

    @Override
    public void onLoad() {
        instance = this;

        rolesComposition = new HashMap<>();
        rolesList = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        UHCAPI.getInstance().getModuleHandler().setModule(new NinjagoModule());

        registerCommands();
        registerListeners();

        rolesComposition.put(Lloyd.class, 1);
    }

    public static Ninjago getInstance() {
        return instance;
    }

    public void registerCommands() {
        getCommand("ni").setExecutor(new CommandNinjago());
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new CycleEvents(), this);
    }

    public Map<Class<? extends Role>, Integer> getRolesComposition() {
        return rolesComposition;
    }

    public void setRolesComposition(Map<Class<? extends Role>, Integer> rolesActivated) {
        this.rolesComposition = rolesActivated;
    }

    public List<Role> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Role> rolesList) {
        this.rolesList = rolesList;
    }
}
