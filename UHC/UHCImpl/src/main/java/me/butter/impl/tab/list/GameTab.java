package me.butter.impl.tab.list;

import me.butter.api.player.UHCPlayer;
import me.butter.impl.tab.AbstractTab;
import net.minecraft.server.v1_8_R3.MinecraftServer;

import java.util.Arrays;

public class GameTab extends AbstractTab {

    public GameTab() {
        super();
        setHeaderLine(1, Arrays.asList(
                "§8- §7Custom UHC §8-", "§8- §rCustom UHC §8-"
        ));
        setHeaderLine(2, Arrays.asList(
                "Tps: " + MinecraftServer.getServer().recentTps[0]
        ));
        setFooterLine(1, "§c⚔ " + 0 + "%§r §8 | §r §7❂ " + 0 + "%§r §8 | §r §3✦ " + 0);
    }

    @Override
    public void updatePlayerTab(UHCPlayer uhcPlayer) {
        if(uhcPlayer == null) return;
        //Modify Footers
        setHeaderLine(1, Arrays.asList(
                "§8- §7Custom UHC §8-", "§8- §rCustom UHC §8-"
        ));
        setHeaderLine(2, Arrays.asList(
                "Tps: " + MinecraftServer.getServer().recentTps[0]
        ));

        setFooterLine(1, " §c⚔ " + uhcPlayer.getStrength() + "%§r §8 | §r §7❂ " + uhcPlayer.getResi() + "%§r §8 | §r §3✦ " + uhcPlayer.getSpeed());
        //Modify Headers
        super.updatePlayerTab(uhcPlayer);
    }
}
