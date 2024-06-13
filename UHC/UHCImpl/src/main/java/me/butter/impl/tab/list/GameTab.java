package me.butter.impl.tab.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.tab.AbstractTab;
import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.util.Arrays;
import java.util.List;

public class GameTab extends AbstractTab {
    public GameTab() {
        super(Arrays.asList(
                "§e>>§m--------------------------------§r§e<<",
                UHCAPI.getInstance().getModuleHandler().hasModule() ? "§l" + UHCAPI.getInstance().getModuleHandler().getModule().getName() : "§lUHC",
                "",
                "§8Tps: " + (int) (MinecraftServer.getServer().recentTps[0] * 100) / 100.0f,
                "§c⚔ 0% §8| §7❂ 0% §8| §3✦ 0%",
                ""
        ), Arrays.asList(
                "",
                "§6@" + (UHCAPI.getInstance().getModuleHandler().hasModule() ? UHCAPI.getInstance().getModuleHandler().getModule().getCreator() : "Butter"),
                "§e>>§m--------------------------------§r§e<<"
        ));
    }

    @Override
    public List<String> modifyHeader(UHCPlayer uhcPlayer, List<String> header) {
        header.set(3, "§8Tps: " + (int) (MinecraftServer.getServer().recentTps[0] * 100) / 100.0f);
        header.set(4, "§c⚔ " + uhcPlayer.getStrength() + "% §8| §7❂ " + uhcPlayer.getResi() + "% §8| §3✦ " + uhcPlayer.getSpeed() + "%");

        return header;
    }
}
