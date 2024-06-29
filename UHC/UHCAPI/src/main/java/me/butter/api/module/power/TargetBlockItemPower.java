package me.butter.api.module.power;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;

import java.util.Set;

public abstract class TargetBlockItemPower extends ItemPower {

    int range;

    public TargetBlockItemPower(String name, Material material, int range, int cooldown, int maxUses) {
        super(name, material, cooldown, maxUses);

        this.range = range;
    }

    public int getRange() {
        return range;
    }

    @Override
    public void onUsePower(UHCPlayer player, Action clickAction) {
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

        Block target = player.getPlayer().getTargetBlock((Set<Material>) null, range);
        if(target == null || target.getType() == Material.AIR) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Aucun bloc n'est visé."));
            return;
        }

        if(onEnable(player, target, clickAction)) {
            uses++;
            lastTimeUsed = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer();
        }
    }

    public boolean onEnable(UHCPlayer player, Block target, Action clickAction) {
        return false;
    }
}
