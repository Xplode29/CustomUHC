package me.butter.impl.tab.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.tab.AbstractTab;
import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.util.Arrays;

public class GameTab extends AbstractTab {
    public GameTab() {
        super();
        setHeaderLine(0, "§e>>--------------------------------<<");
        setHeaderLine(1, UHCAPI.getInstance().getModuleHandler().hasModule() ? "§l" + UHCAPI.getInstance().getModuleHandler().getModule().getName() : "§lUHC");

        setHeaderLine(3, "§8Tps: " + (int) (MinecraftServer.getServer().recentTps[0] * 100) / 100.0f);
        setHeaderLine(4, "§c⚔ 0% §8| §7❂ 0% §8| §3✦ 0%");
        setHeaderLine(5, "");

        //Players

        String creator = UHCAPI.getInstance().getModuleHandler().hasModule() ? UHCAPI.getInstance().getModuleHandler().getModule().getCreator() : "Butter";
        setFooterLine(1, Arrays.asList("§6@" + creator, "§e@" + creator));
        setFooterLine(2, "§e>>--------------------------------<<");
    }

    @Override
    public void updatePlayerTab(UHCPlayer uhcPlayer) {
        if(uhcPlayer == null) return;
        setHeaderLine(3, "§8Tps: " + (int) (MinecraftServer.getServer().recentTps[0] * 100) / 100.0f);
        setHeaderLine(4, "§c⚔ " + uhcPlayer.getStrength() + "% §8| §7❂ " + uhcPlayer.getResi() + "% §8| §3✦ " + uhcPlayer.getSpeed() + "%");
        super.updatePlayerTab(uhcPlayer);
    }
}
