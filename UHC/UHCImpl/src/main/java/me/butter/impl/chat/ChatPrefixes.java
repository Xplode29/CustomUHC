package me.butter.impl.chat;

public enum ChatPrefixes {
    //https://www.digminecraft.com/lists/color_list_pc.php
    //https://gist.github.com/Arcensoth/7be59706aab15429ded8d7586a79f466

    NORMAL(" "),
    JOINED("» "),
    LEFT("« "),
    GLOBAL_INFO("§e│ "),
    PLAYER_INFO("§e│ "),
    ERROR("§c│ "),
    LIST_HEADER("▷ "),
    LIST_ELEMENT("  ◆ "),
    SUBLIST_ELEMENT("    - "),
    SEPARATOR("§m                                                         ");

    private final String prefix;

    ChatPrefixes(String prefix) {
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