package me.butter.impl.commands.host;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.module.roles.RoleType;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatSnippets;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.UHCBase;
import me.butter.impl.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GiveRoleCommand extends AbstractCommand {
    public GiveRoleCommand() {
        super("guillrole");
    }

    @Override
    public boolean isHidden() {
        return true;
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 3) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h guillrole <joueur> <role>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null || target.getPlayer() == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        if(UHCAPI.getInstance().getModuleHandler().hasModule() && UHCAPI.getInstance().getModuleHandler().getModule().hasRoles()) {
            for(RoleType roleType : UHCAPI.getInstance().getModuleHandler().getModule().getRoleComposition()) {
                if(roleType.getName().equalsIgnoreCase(args[2])) {
                    try {
                        Role role = roleType.getRoleClass().newInstance();
                        role.setCamp(roleType.getCamp());

                        role.setUHCPlayer(target);
                        target.setRole(role);

                        role.onGiveRole();

                        for(Power power : role.getPowers()) {
                            if(power instanceof ItemPower) {
                                target.giveItem(((ItemPower) power).getItem(), true);
                            }
                        }

                        if (role instanceof Listener) {
                            Bukkit.getPluginManager().registerEvents((Listener) role, UHCBase.getInstance());
                        }

                        role.onDistributionFinished();
                        ChatSnippets.rolePresentation(target);
                        break;
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }


                }
            }
        }
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length == 2) {
            return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
        }
        if(args.length == 3) {
            if(UHCAPI.getInstance().getModuleHandler().hasModule() && UHCAPI.getInstance().getModuleHandler().getModule().hasRoles()) {
                return UHCAPI.getInstance().getModuleHandler().getModule().getRoleComposition().stream().map(RoleType::getName).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}
