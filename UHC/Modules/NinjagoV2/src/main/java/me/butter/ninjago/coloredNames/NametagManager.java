package me.butter.ninjago.coloredNames;

import me.butter.ninjago.NinjagoV2;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NametagManager implements Listener {

    Map<String, ChatColor> avaliableColors;

    public NametagManager() {

        avaliableColors = new HashMap<>();
        avaliableColors.put("red", ChatColor.RED);
        avaliableColors.put("blue", ChatColor.BLUE);
        avaliableColors.put("green", ChatColor.GREEN);
        avaliableColors.put("yellow", ChatColor.YELLOW);
        avaliableColors.put("dark_red", ChatColor.DARK_RED);
        avaliableColors.put("dark_blue", ChatColor.DARK_BLUE);
        avaliableColors.put("dark_green", ChatColor.DARK_GREEN);
        avaliableColors.put("dark_aqua", ChatColor.DARK_AQUA);
        avaliableColors.put("dark_gray", ChatColor.DARK_GRAY);
        avaliableColors.put("dark_purple", ChatColor.DARK_PURPLE);
        avaliableColors.put("gold", ChatColor.GOLD);
        avaliableColors.put("gray", ChatColor.GRAY);
        avaliableColors.put("light_purple", ChatColor.LIGHT_PURPLE);
        avaliableColors.put("white", ChatColor.WHITE);
        avaliableColors.put("orange", ChatColor.GOLD);

        Bukkit.getPluginManager().registerEvents(this, NinjagoV2.getInstance());
    }

    public Team getTeam(Player player, ChatColor color) {
        Scoreboard scoreboard = player.getScoreboard();

        for(Team team : scoreboard.getTeams()) {
            if (team.getPrefix().equals(color.toString())) {
                return team;
            }
        }

        Team team = scoreboard.registerNewTeam(color.name());
        team.setPrefix(color.toString());
        return team;
    }

    public void setPlayerToTeam(Player player, Team friendlyTeam, String targetName) {
        net.minecraft.server.v1_8_R3.Scoreboard nmsScoreboard = new net.minecraft.server.v1_8_R3.Scoreboard();
        ScoreboardTeam nmsTeam = new ScoreboardTeam(nmsScoreboard, friendlyTeam.getName());
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(nmsTeam, Arrays.asList(targetName), 3);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    public Map<String, ChatColor> getAvaliableColors() {
        return avaliableColors;
    }
}
