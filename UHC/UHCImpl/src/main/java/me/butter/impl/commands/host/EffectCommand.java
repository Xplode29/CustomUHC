package me.butter.impl.commands.host;

import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.commands.AbstractCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EffectCommand extends AbstractCommand {
    
    public EffectCommand() {
        super("effect");
    }

    @Override
    public void onCommand(UHCPlayer sender, String command, String[] args) {
        if(args.length < 5) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h effect <joueur> <add|remove> <speed|force|resistance> <amount>"));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);

        if(target == null) {
            sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Le joueur " + args[1] + " n'existe pas !"));
            return;
        }

        int amount = Integer.parseInt(args[4]);

        if(args[2].equalsIgnoreCase("add")) {
            if(args[3].equalsIgnoreCase("speed")) {
                target.addSpeed(amount);
                return;
            }
            else if(args[3].equalsIgnoreCase("force")) {
                target.addStrength(amount);
                return;
            }
            else if(args[3].equalsIgnoreCase("resistance")) {
                target.addResi(amount);
                return;
            }
        }
        else if(args[2].equalsIgnoreCase("remove")) {
            if(args[3].equalsIgnoreCase("speed")) {
                target.removeSpeed(amount);
                return;
            }
            else if(args[3].equalsIgnoreCase("force")) {
                target.removeStrength(amount);
                return;
            }
            else if(args[3].equalsIgnoreCase("resistance")) {
                target.removeResi(amount);
                return;
            }
        }

        sender.getPlayer().sendMessage(ChatUtils.ERROR.getMessage("Usage : /h effect <joueur> <add|remove> <speed|force|resistance>"));
    }

    @Override
    public List<String> onTabComplete(UHCPlayer sender, String command, String[] args) {
        if(args.length == 2) {
            return UHCAPI.getInstance().getPlayerHandler().getPlayers().stream().map(UHCPlayer::getName).collect(Collectors.toList());
        }
        if(args.length == 3) {
            return Lists.newArrayList("add", "remove");
        }
        if(args.length == 4) {
            return Lists.newArrayList("speed", "force", "resistance");
        }
        return new ArrayList<>();
    }
}
