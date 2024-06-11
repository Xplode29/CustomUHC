package me.butter.ninjago;

import me.butter.api.UHCAPI;
import me.butter.api.module.camp.Camp;
import me.butter.api.module.roles.Role;
import me.butter.api.module.roles.RoleType;
import me.butter.ninjago.items.goldenWeapons.GoldenWeaponManager;
import me.butter.ninjago.scenarios.GoldenNinjaScenario;
import me.butter.ninjago.commands.CommandNinjago;
import me.butter.ninjago.listener.CycleEvents;
import me.butter.ninjago.roles.RoleEnum;
import me.butter.ninjago.timers.GoldenWeaponsTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Ninjago extends JavaPlugin {

    private static Ninjago instance;
    private List<Role> rolesList;

    private GoldenWeaponManager goldenWeaponManager;

    @Override
    public void onLoad() {
        instance = this;

        rolesList = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        goldenWeaponManager = new GoldenWeaponManager();

        UHCAPI.getInstance().getModuleHandler().setModule(new NinjagoModule());

        UHCAPI.getInstance().getScenarioHandler().addScenario(new GoldenNinjaScenario());
        UHCAPI.getInstance().getTimerHandler().addTimer(new GoldenWeaponsTimer());

        registerCommands();
        registerListeners();
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

    public List<RoleType> getRolesComposition() {
        List<RoleType> roleList = new ArrayList<>();
        for(RoleEnum role : RoleEnum.values()) {
            roleList.add(new RoleType() {
                @Override
                public Class<? extends Role> getRoleClass() {
                    return role.getRoleClass();
                }

                @Override
                public Camp getCamp() {
                    return role.getCampEnum().getCamp();
                }

                @Override
                public String getName() {
                    return role.getName();
                }

                @Override
                public Material getIcon() {
                    return role.getIcon();
                }

                @Override
                public int getAmount() {
                    return role.getAmount();
                }
            });
        }
        return roleList;
    }

    public List<Role> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Role> rolesList) {
        this.rolesList = rolesList;
    }

    public GoldenWeaponManager getGoldenWeaponManager() {
        return goldenWeaponManager;
    }
}
