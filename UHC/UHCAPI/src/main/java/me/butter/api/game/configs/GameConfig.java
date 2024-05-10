package me.butter.api.game.configs;

import me.butter.api.player.UHCPlayer;

public interface GameConfig {

    UHCPlayer getHost(); void setHost(UHCPlayer host);

    int getMaxPlayers(); void setMaxPlayers(int maxPlayers);

    boolean isStarting(); void setStarting(boolean starting);

    int getTimer(); void setTimer(int timer);

    boolean isDay(); void setDay(boolean day);

    boolean isInvincibility(); void setInvincibility(boolean invincibility);

    boolean isPvp(); void setPvp(boolean pvp);

    boolean isMeetup(); void setMeetup(boolean meetup);

    boolean isChatEnabled(); void setChatEnabled(boolean enabled);
}
