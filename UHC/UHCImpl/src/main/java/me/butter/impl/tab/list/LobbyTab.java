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
                "Tps: " + MinecraftServer.getServer().recentTps[0]
        ));
    }

    @Override
    public void updatePlayerTab(UHCPlayer uhcPlayer) {
        if(uhcPlayer == null) return;
        //Modify Footers
        setHeaderLine(1, Arrays.asList(
                "§8- §7Lobby §8-", "§8- §rLobby §8-"
        ));
        setHeaderLine(2, Arrays.asList(
                "Tps: " + MinecraftServer.getServer().recentTps[0]
        ));

        super.updatePlayerTab(uhcPlayer);
    }
}
