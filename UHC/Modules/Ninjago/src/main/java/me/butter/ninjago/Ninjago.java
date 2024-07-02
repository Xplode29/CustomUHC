package me.butter.ninjago;

import me.butter.api.UHCAPI;
import me.butter.api.module.camp.Camp;
import me.butter.api.module.roles.Role;
import me.butter.api.module.roles.RoleType;
import me.butter.ninjago.commands.NinjagoCommand;
import me.butter.ninjago.goldenNinja.GoldenNinjaScenario;
import me.butter.ninjago.goldenWeapons.GoldenWeaponManager;
import me.butter.ninjago.goldenWeapons.GoldenWeaponsScenario;
import me.butter.ninjago.listener.CycleEvents;
import me.butter.ninjago.roles.RoleEnum;
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
    }

    @Override
    public void onEnable() {
        rolesList = new ArrayList<>();

        UHCAPI.getInstance().getModuleHandler().setModule(new NinjagoModule());

        goldenWeaponManager = new GoldenWeaponManager();

        UHCAPI.getInstance().getScenarioHandler().addScenario(new GoldenNinjaScenario());
        UHCAPI.getInstance().getScenarioHandler().addScenario(new GoldenWeaponsScenario());

        registerCommands();
        registerListeners();

        for(RoleEnum role : RoleEnum.values()) {
            role.setAmount(0);
        }
    }

    public static Ninjago getInstance() {
        return instance;
    }

    public void registerCommands() {
        getCommand("n").setExecutor(new NinjagoCommand());
    }

    public void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new CycleEvents(), this);
    }

    public List<RoleType> getRolesComposition() {
        List<RoleType> roleList = new ArrayList<>();
        for(RoleEnum role : RoleEnum.values()) {
            if(role.getAmount() <= 0) continue;
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
