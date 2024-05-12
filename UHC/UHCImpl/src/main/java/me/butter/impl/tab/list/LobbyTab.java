package me.butter.impl.tab.list;

import me.butter.impl.tab.AbstractTab;
import org.bukkit.Bukkit;

import java.util.Arrays;

public class LobbyTab extends AbstractTab {

    public LobbyTab() {
        super();
        setHeaderLine(1, Arrays.asList(
                "§8- §7Lobby §8-", "§8- §rLobby §8-"
        ));
    }
}
