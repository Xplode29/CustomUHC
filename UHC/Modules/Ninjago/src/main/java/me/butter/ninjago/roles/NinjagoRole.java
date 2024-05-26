package me.butter.ninjago.roles;

import me.butter.api.module.camp.Camp;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class NinjagoRole implements Role, Listener {

    String name;
    String doc;

    List<Power> powers;
    UHCPlayer uhcPlayer;

    Camp camp;

    public NinjagoRole(String name, String doc, List<Power> powers) {
        this.name = name;
        this.doc = doc;
        this.powers = powers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    public String getDoc() {
        return doc;
    }

    @Override
    public Camp getCamp() {
        return camp;
    }

    @Override
    public void setCamp(Camp camp) {
        this.camp = camp;
    }

    @Override
    public List<String> additionalDescription() {
        return new ArrayList<>();
    }

    @Override
    public List<Power> getPowers() {
        return powers;
    }

    @Override
    public void setPowers(List<Power> powers) {
        this.powers = powers;
    }

    @Override
    public void addPower(Power power) {
        if(powers.contains(power)) return;
        powers.add(power);
    }

    @Override
    public void removePower(Class<? extends Power> powerClass) {
        powers.removeIf(power -> power.getClass().equals(powerClass));
    }

    @Override
    public boolean hasItems() {
        for(Power power : getPowers()) {
            if(power instanceof ItemPower) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasCommands() {
        for(Power power : getPowers()) {
            if(power instanceof CommandPower) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UHCPlayer getUHCPlayer() {
        return uhcPlayer;
    }

    @Override
    public void setUHCPlayer(UHCPlayer uhcPlayer) {
        this.uhcPlayer = uhcPlayer;
    }

    @Override
    public void onGiveRole() {

    }

    @Override
    public void onDistributionFinished() {

    }

    @Override
    public void onDay() {

    }

    @Override
    public void onNight() {

    }

    public boolean isNinja() {
        return false;
    }

    public boolean isInList() {
        return false;
    }
}
