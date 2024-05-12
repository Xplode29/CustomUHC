package me.butter.impl.game.configs;

import me.butter.api.game.configs.GameConfig;
import me.butter.api.player.UHCPlayer;

public class GameConfigImpl implements GameConfig {

    UHCPlayer host;

    int maxPlayers, timer, groupSize;

    boolean day, starting, invincibility, pvp, meetup, chatEnabled;

    public GameConfigImpl() {
        this.maxPlayers = 39;
        this.groupSize = 6;
        this.timer = 0;
        this.day = true;
        this.invincibility = true;
        this.pvp = false;
        this.meetup = false;
        this.chatEnabled = true;
    }

    @Override
    public UHCPlayer getHost() {
        return host;
    }

    @Override
    public void setHost(UHCPlayer host) {
        this.host = host;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public int getGroupSize() {
        return groupSize;
    }

    @Override
    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    @Override
    public boolean isStarting() {
        return starting;
    }

    @Override
    public void setStarting(boolean starting) {
        this.starting = starting;
    }

    @Override
    public int getTimer() {
        return timer;
    }

    @Override
    public void setTimer(int timer) {
        this.timer = timer;
    }

    @Override
    public boolean isDay() {
        return day;
    }

    @Override
    public void setDay(boolean day) {
        this.day = day;
    }

    @Override
    public boolean isInvincibility() {
        return invincibility;
    }

    @Override
    public void setInvincibility(boolean invincibility) {
        this.invincibility = invincibility;
    }

    @Override
    public boolean isPvp() {
        return pvp;
    }

    @Override
    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    @Override
    public boolean isMeetup() {
        return meetup;
    }

    @Override
    public void setMeetup(boolean meetup) {
        this.meetup = meetup;
    }

    @Override
    public boolean isChatEnabled() {
        return chatEnabled;
    }

    @Override
    public void setChatEnabled(boolean enabled) {
        this.chatEnabled = enabled;
    }
}
