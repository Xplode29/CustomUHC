package me.butter.impl.game.configs;

import me.butter.api.game.configs.GameConfig;
import me.butter.api.player.UHCPlayer;

import java.util.ArrayList;
import java.util.List;

public class GameConfigImpl implements GameConfig {

    private UHCPlayer host;

    private List<UHCPlayer> coHosts;

    private int maxPlayers, timer, groupSize, dayDuration, episode, episodeDuration;

    private boolean starting, day, doesDayCycle, invincibility, pvp, meetup, chatEnabled;

    public GameConfigImpl() {
        coHosts = new ArrayList<>();

        this.maxPlayers = 39;
        this.groupSize = 6;
        this.starting = false;

        this.timer = 0;
        this.day = true;
        this.doesDayCycle = true;
        this.dayDuration = 10;
        this.episode = 0;
        this.episodeDuration = 20;

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
    public List<UHCPlayer> getCoHosts() {
        return coHosts;
    }

    @Override
    public void setCoHosts(List<UHCPlayer> coHosts) {
        this.coHosts = coHosts;
    }

    @Override
    public void addCoHost(UHCPlayer coHost) {
        if(coHosts.contains(coHost)) return;
        coHosts.add(coHost);
    }

    @Override
    public void removeCoHost(UHCPlayer coHost) {
        coHosts.remove(coHost);
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
    public boolean isDayCycleActivated() {
        return doesDayCycle;
    }

    @Override
    public void setDayCycleActivated(boolean doesDayCycle) {
        this.doesDayCycle = doesDayCycle;
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
    public int getDayDuration() {
        return dayDuration;
    }

    @Override
    public void setDayDuration(int duration) {
        this.dayDuration = duration;
    }

    @Override
    public int getEpisode() {
        return episode;
    }

    @Override
    public void setEpisode(int episode) {
        this.episode = episode;
    }

    @Override
    public int getEpisodeDuration() {
        return episodeDuration;
    }

    @Override
    public void setEpisodeDuration(int duration) {
        this.episodeDuration = duration;
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
    public boolean isPVP() {
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
