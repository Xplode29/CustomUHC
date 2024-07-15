package me.butter.impl.nametagColor;

import me.butter.api.UHCAPI;
import me.butter.api.nametagColor.NametagColorHandler;
import me.butter.api.player.UHCPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NametagColorHandlerImpl implements NametagColorHandler {

    Map<String, ChatColor> avaliableColors;

    public NametagColorHandlerImpl() {
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
    }

    public Map<String, ChatColor> getAvaliableColors() {
        return avaliableColors;
    }

    @Override
    public Team getTeam(UHCPlayer sender, ChatColor color, boolean visible) {
        Player player = sender.getPlayer();
        if (player == null) return null;

        Scoreboard scoreboard = player.getScoreboard();

        String teamName = color.name() + (visible ? "_v" : "_i");

        for(Team team : scoreboard.getTeams()) {
            if (team.getName().equals(teamName)) {
                return team;
            }
        }

        Team team = scoreboard.registerNewTeam(teamName);
        team.setPrefix(color.toString());

        if(!visible) team.setNameTagVisibility(NameTagVisibility.NEVER);
        return team;
    }

    @Override
    public void setPlayerToTeam(UHCPlayer sender, ChatColor color, UHCPlayer target) {
        sender.addColoredNametag(target, color);

        Player player = sender.getPlayer();
        if (player == null) return;

        Team team = getTeam(sender, color, target.isNameTagVisible());

        net.minecraft.server.v1_8_R3.Scoreboard nmsScoreboard = new net.minecraft.server.v1_8_R3.Scoreboard();
        ScoreboardTeam nmsTeam = new ScoreboardTeam(nmsScoreboard, team.getName());
        PacketPlayOutScoreboardTeam packet = new PacketPlayOutScoreboardTeam(nmsTeam, Collections.singletonList(target.getName()), 3);

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    @Override
    public void updatePlayerColor(UHCPlayer target) {
        for(UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getAllPlayers()) {
            if(player.getColoredNametags().containsKey(target)) {
                setPlayerToTeam(player, player.getColoredNametags().get(target), target);
            }
        }
    }
}
