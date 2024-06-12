package me.butter.api.utils.chat;

import org.bukkit.ChatColor;

public enum ChatUtils {
    //https://www.digminecraft.com/lists/color_list_pc.php
    //https://gist.github.com/Arcensoth/7be59706aab15429ded8d7586a79f466

    NORMAL(" "),
    JOINED(ChatColor.DARK_GRAY + "» "),
    LEFT(ChatColor.DARK_GRAY + "« "),
    GLOBAL_INFO("§8[§e§lGlobal§r§8]§r "),
    PLAYER_INFO("§8[§e§lPlayer§r§8]§r "),
    ERROR("§c│ "),
    WARNING("§c│ "),
    LIST_HEADER("▷ "),
    LIST_ELEMENT("  ◆ "),
    SUBLIST_ELEMENT("    - "),
    SEPARATOR("§m                                                    ");

    public final String prefix;

    ChatUtils(String prefix) {
        this.prefix = prefix;
    }

    public String toString() {
        return prefix;
    }

    public String getMessage(String message) {
        return prefix + message;
    }

    public static String formatScoreboard(String name, String value) {
        return ChatUtils.JOINED.prefix + "§c" + name + " : §r" + value;
    }
    public static String formatScoreboard(String name, boolean condition) {
        return ChatUtils.JOINED.prefix + "§c" + name + " : §r" + (condition ? "§a✔" : "§c✘");
    }
}