package me.butter.impl.tab.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.tab.AbstractTab;
import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.util.Arrays;
import java.util.List;

public class MainTab extends AbstractTab {

    boolean changed = false;

    public MainTab() {
        super(Arrays.asList(
                "§e>>§m--------------------------------§r§e<<",
                "§lUHC",
                "",
                "§8Tps: " + (int) (MinecraftServer.getServer().recentTps[0] * 100) / 100.0f,
                ""
        ), Arrays.asList(
                "",
                "§c⚔ 0% §8| §7❂ 0% §8| §3✦ 0%",
                "",
                "§6@" + (UHCAPI.getInstance().getModuleHandler().hasModule() ? UHCAPI.getInstance().getModuleHandler().getModule().getCreator() : "ButterOnPancakes"),
                "§e>>§m--------------------------------§r§e<<"
        ));
    }

    @Override
    public List<String> modifyHeader(UHCPlayer uhcPlayer, List<String> header) {
        if(!changed && UHCAPI.getInstance().getModuleHandler().hasModule()) {
            header.set(1, "§l" + UHCAPI.getInstance().getModuleHandler().getModule().getName());
            changed = true;
        }
        header.set(3, "§8Tps: " + (int) (MinecraftServer.getServer().recentTps[0] * 100) / 100.0f);
        return header;
    }

    @Override
    public List<String> modifyFooter(UHCPlayer uhcPlayer, List<String> footer) {
        footer.set(1, "§c⚔ " + uhcPlayer.getStrength() + "% §8| §7❂ " + uhcPlayer.getResi() + "% §8| §3✦ " + uhcPlayer.getSpeed() + "%");
        footer.set(3, "§6@" + (UHCAPI.getInstance().getModuleHandler().hasModule() ? UHCAPI.getInstance().getModuleHandler().getModule().getCreator() : "ButterOnPancakes"));
        return footer;
    }
}
