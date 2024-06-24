package me.butter.api.module.power;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;

public abstract class Power {

    String name;
    int cooldown, lastTimeUsed, uses, maxUses;

    public Power(String name, int cooldown, int maxUses) {
        this.name = name;
        this.cooldown = cooldown;
        this.maxUses = maxUses;
        this.lastTimeUsed = -cooldown;
    }

    public String getName() {
        return name;
    }

    public String[] getDescription() {
        return new String[0];
    }

    public void onUsePower(UHCPlayer player) {
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
        if(onEnable(player)) {
            uses++;
            lastTimeUsed = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer();
        }
    }

    public boolean onEnable(UHCPlayer player) {
        return false;
    }

    public void forceCooldown() {
        uses++;
        lastTimeUsed = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer();
    }

    public int getMaxUses() {
        return maxUses;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void reset() {
        uses = 0;
        lastTimeUsed = -cooldown;
    }

    public boolean hideCooldowns() {
        return false;
    }

    public boolean showPower() {
        return true;
    }
}
