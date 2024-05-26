package me.butter.api.module.power;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

public abstract class RightClickItemPower extends ItemPower {

    public RightClickItemPower(String name, Material material, int cooldown, int maxUses) {
        super(name, material, cooldown, maxUses);
    }

    public void onUsePower(UHCPlayer player, Action clickAction) {
        if(clickAction == Action.LEFT_CLICK_AIR || clickAction == Action.LEFT_CLICK_BLOCK) return;

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
        if(onEnable(player, clickAction)) {
            uses++;
            lastTimeUsed = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer();
        }
    }

    public boolean onEnable(UHCPlayer player, Action clickAction) {
        return false;
    }
}
