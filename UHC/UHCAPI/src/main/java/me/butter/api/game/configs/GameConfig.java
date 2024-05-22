package me.butter.api.game.configs;

import me.butter.api.player.UHCPlayer;

public interface GameConfig {

    UHCPlayer getHost(); void setHost(UHCPlayer host);

    int getMaxPlayers(); void setMaxPlayers(int maxPlayers);

    int getGroupSize(); void setGroupSize(int groupSize);

    boolean isStarting(); void setStarting(boolean starting);

    int getTimer(); void setTimer(int timer);

    boolean isDayCycleActivated(); void setDayCycleActivated(boolean doesDayCycle);

    boolean isDay(); void setDay(boolean day);

    int getDayDuration(); void setDayDuration(int duration);

    int getEpisode(); void setEpisode(int episode);

    int getEpisodeDuration(); void setEpisodeDuration(int duration);

    boolean isInvincibility(); void setInvincibility(boolean invincibility);

    boolean isPVP(); void setPvp(boolean pvp);

    boolean isMeetup(); void setMeetup(boolean meetup);

    boolean isChatEnabled(); void setChatEnabled(boolean enabled);
}
