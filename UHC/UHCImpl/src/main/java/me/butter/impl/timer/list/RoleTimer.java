package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.module.roles.RoleType;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatSnippets;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.UHCImpl;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RoleTimer extends AbstractTimer {

    public RoleTimer() {
        super("Obtention des roles", Material.ENDER_PEARL, 10);
    }

    @Override
    public void onTimerDone() {
        List<Role> roleList = new ArrayList<>();

        List<RoleType> roleComposition = UHCAPI.getInstance().getModuleHandler().getModule().getRoleComposition();
        Collections.reverse(roleComposition);

        List<UHCPlayer> players = new ArrayList<>(UHCAPI.getInstance().getPlayerHandler().getPlayersInGame());
        Collections.shuffle(players, new Random());

        int index = 0;
        for(RoleType roleType : roleComposition) {
            if(roleType.getAmount() == 0 || index >= UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size()) continue;
            for(int j = 0; j < roleType.getAmount(); j++) {
                try {
                    Role role = roleType.getRoleClass().newInstance();
                    role.setCamp(roleType.getCamp());
                    roleList.add(role);
                    index++;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Collections.shuffle(roleList, new Random());

        if(roleList.size() < players.size()) {
            Bukkit.broadcastMessage(ChatUtils.ERROR.getMessage("Il n'y a pas assez de roles actifs !"));
            return;
        }

        for(int i = 0; i < players.size(); i++) {
            UHCPlayer player = players.get(i);
            Role role = roleList.get(i);
            if(player == null || role == null) continue;
            if(player.getRole() != null) continue;

            role.setUHCPlayer(player);
            player.setRole(role);

            role.onGiveRole();

            for(Power power : role.getPowers()) {
                if(power instanceof ItemPower) {
                    player.giveItem(((ItemPower) power).getItem(), true);
                }
            }

            if (role instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) role, UHCImpl.getInstance());
            }
        }

        UHCAPI.getInstance().getModuleHandler().getModule().setRolesList(roleList);

        for(UHCPlayer player : players) {
            if(player.getRole() == null) continue;

            player.getRole().onDistributionFinished();
            ChatSnippets.rolePresentation(player);
        }
    }

    @Override
    public void onUpdate(int timer) {
        if ((getMaxTimer() - 5 * 60) == timer) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Les roles seront attribués dans 5 minutes !"));
        }
        else if ((getMaxTimer() - 2 * 60) == timer) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Les roles seront attribués dans 2 minutes !"));
        }
        else if ((getMaxTimer() - 60) == timer) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Les roles seront attribués dans 1 minutes !"));
        }
        else if ((getMaxTimer() - 30) == timer) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Les roles seront attribués dans 30 secondes !"));
        }
        else if (getMaxTimer() - timer <= 10) {
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Les roles seront attribués dans " + (getMaxTimer() - timer)  +" secondes !"));
        }
    }
}
