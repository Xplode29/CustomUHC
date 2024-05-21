package me.butter.api.utils;

import org.bukkit.ChatColor;

public enum ChatUtils {
    //https://www.digminecraft.com/lists/color_list_pc.php
    //https://gist.github.com/Arcensoth/7be59706aab15429ded8d7586a79f466

    NORMAL(" "),
    JOINED(ChatColor.DARK_GRAY + "» "),
    LEFT(ChatColor.DARK_GRAY + "« "),
    GLOBAL_INFO("§e│ "),
    PLAYER_INFO("§e│ "),
    ERROR("§c│ "),
    WARNING("§c│ "),
    LIST_HEADER("▷ "),
    LIST_ELEMENT("  ◆ "),
    SUBLIST_ELEMENT("    - "),
    SEPARATOR("§m                                                    ");

    private final String prefix;

    ChatUtils(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getColoredPrefix(String colorCode) {
        return colorCode + prefix;
    }

    public String getMessage(String message) {
        return prefix + message;
    }

    public String getColoredMessage(String colorCode, String message) {
        return colorCode + prefix + message;
    }
}