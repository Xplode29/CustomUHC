package me.butter.impl.timer.list;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatSnippets;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.UHCImpl;
import me.butter.impl.timer.AbstractTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoleTimer extends AbstractTimer {

    public RoleTimer() {
        super("Obtention des roles", Material.ENDER_PEARL, 10);
    }

    @Override
    public void onTimerDone() {
        List<Role> roleList = new ArrayList<>();

        int index = 0;
        for(Map.Entry<Class<? extends Role>, Integer> role : UHCAPI.getInstance().getModuleHandler().getModule().getRoleComposition().entrySet()) {
            if(role.getValue() == 0 || index >= UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size()) continue;
            for(int j = 0; j < role.getValue(); j++) {
                try {
                    roleList.add(role.getKey().newInstance());
                    index++;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(roleList.size() < UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size()) {
            Bukkit.broadcastMessage(ChatUtils.ERROR.getMessage("Il n'y a pas assez de roles actifs !"));
            Bukkit.broadcastMessage(roleList + "/" + UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().toString());
            return;
        }

        for(int i = 0; i < UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size(); i++) {
            UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().get(i);
            Role role = roleList.get(i);
            if(player == null || role == null) continue;
            if(player.getRole() != null) continue;

            role.setUHCPlayer(player);
            player.setRole(role);

            ChatSnippets.rolePresentation(player);

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

        for(Role role : roleList) {
            role.onDistributionFinished();
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
