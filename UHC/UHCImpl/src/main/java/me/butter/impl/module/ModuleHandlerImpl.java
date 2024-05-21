package me.butter.impl.module;

import me.butter.api.UHCAPI;
import me.butter.api.module.Module;
import me.butter.api.module.ModuleHandler;
import me.butter.impl.timer.list.RoleTimer;

public class ModuleHandlerImpl implements ModuleHandler {

    Module module;

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public void setModule(Module module) {
        this.module = module;

        if(module.hasRoles()) {
            UHCAPI.getInstance().getTimerHandler().addTimer(new RoleTimer());
        }
    }

    @Override
    public boolean hasModule() {
        return module != null;
    }
}
