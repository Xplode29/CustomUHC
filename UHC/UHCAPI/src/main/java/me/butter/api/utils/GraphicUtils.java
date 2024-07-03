package me.butter.api.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

public class GraphicUtils {

    public static String convertToAccurateTime(int time) {
        if(time >= 3600) {
            return String.format("%dh %dm %ds", (time) / 3600, ((time) / 60) % 60, (time) % 60);
        } else if (time >= 60) {
            return String.format("%dm %ds", ((time) / 60) % 60, (time) % 60);
        } else {
            return String.format("%ds", (time) % 60);
        }
    }

    public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor, ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return com.google.common.base.Strings.repeat("" + completedColor + symbol, progressBars)
                + com.google.common.base.Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }

    public static Color convertToColor(ChatColor color) {
        switch (color) {
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case RED:
                return Color.RED;
            case YELLOW:
                return Color.YELLOW;
            case AQUA:
                return Color.AQUA;
            case GRAY:
                return Color.GRAY;
            case BLACK:
                return Color.BLACK;
            case GOLD:
                return Color.ORANGE;
            default:
                return Color.WHITE;
        }
    }
}
