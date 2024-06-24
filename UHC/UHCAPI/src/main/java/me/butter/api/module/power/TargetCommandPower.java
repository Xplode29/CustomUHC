package me.butter.api.module.power;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;

public class TargetCommandPower extends CommandPower{
    public TargetCommandPower(String name, String argument, int cooldown, int maxUses) {
        super(name, argument, cooldown, maxUses);
    }

    public void onUsePower(UHCPlayer player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Usage: /n " + argument + "<joueur>"));
            return;
        }

        if(uses >= maxUses && maxUses != -1) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez déjà utilisé " + uses + " fois ce pouvoir."));
            return;
        }
        if(lastTimeUsed + cooldown > UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Vous devez attendre " +
                    GraphicUtils.convertToAccurateTime(cooldown + lastTimeUsed - UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) +
                    " avant d'utiliser ce pouvoir."));
            return;
        }

        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(args[1]);
        if(target == null) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Veuillez indiquer un joueur correct !"));
            return;
        }

        if(onEnable(player, target, args)) {
            uses++;
            lastTimeUsed = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer();
        }
    }

    public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
        return false;
    }
}
