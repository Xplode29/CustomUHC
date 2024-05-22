package me.butter.impl.tab.list;

import me.butter.api.player.UHCPlayer;
import me.butter.impl.tab.AbstractTab;
import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.util.Arrays;

public class LobbyTab extends AbstractTab {

    public LobbyTab() {
        super();
        setHeaderLine(1, Arrays.asList(
                "§8- §7Lobby §8-", "§8- §rLobby §8-"
        ));
        setHeaderLine(2, Arrays.asList(
                "Tps: " + (int) (MinecraftServer.getServer().recentTps[0] * 10) / 10
        ));
        setFooterLine(1, Arrays.asList(
                "§6@ButterOnPancakes (avec l'aide de @Hyro)", "§e@ButterOnPancakes (avec l'aide de @Hyro)"
        ));
    }

    @Override
    public void updatePlayerTab(UHCPlayer uhcPlayer) {
        if(uhcPlayer == null) return;
        setHeaderLine(2, Arrays.asList(
                "Tps: " + (int) (MinecraftServer.getServer().recentTps[0] * 10) / 10
        ));
        super.updatePlayerTab(uhcPlayer);
    }
}
