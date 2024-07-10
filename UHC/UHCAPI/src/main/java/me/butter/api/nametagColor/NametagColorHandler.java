package me.butter.api.nametagColor;

import me.butter.api.player.UHCPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Map;

public interface NametagColorHandler {

    Map<String, ChatColor> getAvaliableColors();

    Team getTeam(UHCPlayer sender, ChatColor color, boolean visible);

    void setPlayerToTeam(UHCPlayer sender, ChatColor color, UHCPlayer target);

    void updatePlayerColor(UHCPlayer player);
}
