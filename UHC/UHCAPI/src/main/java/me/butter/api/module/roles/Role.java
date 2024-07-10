package me.butter.api.module.roles;

import me.butter.api.module.camp.Camp;
import me.butter.api.module.power.Power;
import me.butter.api.player.UHCPlayer;

import java.util.List;

public interface Role {
    String getName(); String[] getDescription(); String getDoc();

    Camp getCamp(); void setCamp(Camp camp);
    Camp getStartCamp(); void setStartCamp(Camp camp);

    List<String> additionalDescription();

    List<Power> getPowers(); void setPowers(List<Power> powers);
    void addPower(Power power); void removePower(Class<? extends Power> powerClass);

    boolean hasItems(); boolean hasCommands();

    UHCPlayer getUHCPlayer(); void setUHCPlayer(UHCPlayer uhcPlayer);

    void onGiveRole();

    void onDistributionFinished();

    void onDay(); void onNight();
}
