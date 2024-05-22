package me.butter.impl.tab.list;

import me.butter.api.UHCAPI;
import me.butter.impl.tab.AbstractTab;

import java.util.Arrays;

public class GameTab extends AbstractTab {
    public GameTab() {
        super();
        setHeaderLine(1, UHCAPI.getInstance().getModuleHandler().hasModule() ? "§l" + UHCAPI.getInstance().getModuleHandler().getModule().getName() : "§lUHC");
        if(UHCAPI.getInstance().getModuleHandler().hasModule()) {
            setHeaderLine(3, Arrays.asList(
                    "§6@" + UHCAPI.getInstance().getModuleHandler().getModule().getCreator(),
                    "§e@" + UHCAPI.getInstance().getModuleHandler().getModule().getCreator()
            ));
        }
        setFooterLine(1, Arrays.asList("§6@Butter", "§e@Butter"));
    }
}
