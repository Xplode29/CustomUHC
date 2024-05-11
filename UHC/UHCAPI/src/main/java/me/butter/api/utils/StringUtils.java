package me.butter.api.utils;

public class StringUtils {
    public static String convertToAccurateTime(int time) {
        if(time >= 3600) {
            return String.format("%dh %dm %ds", (time) / 3600, ((time) / 60) % 60, (time) % 60);
        } else if (time >= 60) {
            return String.format("%dm %ds", ((time) / 60) % 60, (time) % 60);
        } else {
            return String.format("%ds", (time) % 60);
        }
    }
}
