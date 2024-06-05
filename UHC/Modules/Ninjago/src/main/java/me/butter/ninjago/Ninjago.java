package me.butter.ninjago;

import me.butter.api.UHCAPI;
import me.butter.api.module.camp.Camp;
import me.butter.api.module.roles.Role;
import me.butter.api.module.roles.RoleType;
import me.butter.api.structures.Structure;
import me.butter.ninjago.structures.list.StructChestHolder;
import me.butter.ninjago.commands.CommandNinjago;
import me.butter.ninjago.listener.CycleEvents;
import me.butter.ninjago.roles.RoleEnum;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Ninjago extends JavaPlugin {

    private static Ninjago instance;
    private List<Role> rolesList;

    @Override
    public void onLoad() {
        instance = this;

        rolesList = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        UHCAPI.getInstance().getModuleHandler().setModule(new NinjagoModule());

        for(int i = 0; i < 1; i++) {
            Structure struct = new StructChestHolder(0, 100, 0);
            UHCAPI.getInstance().getStructureHandler().addStructure(struct);
        }

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
}
